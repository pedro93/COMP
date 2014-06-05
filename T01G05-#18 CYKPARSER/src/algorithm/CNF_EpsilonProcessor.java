package algorithm;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class CNF_EpsilonProcessor {
	
	private static final String ERASE_ME = "ERASE_ME";
	private static final String START0 = "START0" ;
	private static final String START = "START" ;
	private static final String EPSILON = "epsilon" ;
	
	private boolean doThisAgain ;
	private boolean hasNewStartProd ;
	
	private static Vector<String> emptyProds ;
	private static Vector<Vector<String>> productions ;
	private static Map<Integer, Vector<String> > newProds ;
	
	public CNF_EpsilonProcessor( Vector<Vector<String>> prods , boolean newstart ) 
	{	
		emptyProds = new Vector<String>() ;
		newProds = new HashMap<Integer, Vector<String>>() ;
		productions = prods ;
		doThisAgain = false ;
		hasNewStartProd = newstart ;
	}

	/**
	 * metodo publico que engloba todos os passos de processamento
	 * das producoes com epsilons presentes na gramatica
	 */
	public void calculateNewProds() 
	{
		tagEmptyProds() ;
		
		doThisAgain = false ;
		
		for( Vector<String> prod : productions )
			generateAllPossibleProds( prod ) ;
		
		if ( doThisAgain )
		{
			emptyProds.clear();
			cleanUpProdutions() ;
			addNewProductions() ;
			calculateNewProds() ;
		}
	}
	
	/**
	 * perante um vetor de producoes, esta funcao marca todas as producoes
	 * com transicoes para epsilon
	 */
	private void tagEmptyProds() {
		
		for( Vector<String> prod : productions )
		{
			// verificar se uma producao tem o simbolo do vazio
			for( int i=2 ; i<prod.size() ; i++ )
				if( prod.elementAt(i).equals(EPSILON) )
				{
					if ( this.hasNewStartProd ) 
					{
						if( !(prod.elementAt(0).equals(START0) ) )
						{
							emptyProds.add(prod.elementAt(0)) ; // adicionado ao vetor para depois eliminar em todo o lado
							prod.clear() ;
							prod.add(ERASE_ME) ; 
						}
					}
					else  
					{
						if( !(prod.elementAt(0).equals(START) ) )
						{
							emptyProds.add(prod.elementAt(0)) ; // adicionado ao vetor para depois eliminar em todo o lado
							prod.clear() ;
							prod.add(ERASE_ME) ; 
						}
					}
				}
		}
	}
	
	/**
	 * funcao geradora de todas as producoes sem epsilons a partir da producao 'prod'
	 * @param prod
	 */
	private void generateAllPossibleProds( Vector<String> prod )
	{
		for( int i=2 ; i<prod.size() ; i++ )
		{
			for( String empty_prod : emptyProds )
				if( prod.elementAt(i).equals(empty_prod) )
				{
					Vector<String> new_prod = new Vector<String>() ;
					for( int k=0 ; k<prod.size() ; k++ )
						if( ! (k==i) )
							new_prod.add( prod.elementAt(k) ) ;
					
					if( new_prod.size() == 2 )
					{
						new_prod.add(EPSILON) ;
						doThisAgain = true ;
					}
					
					int key = new_prod.hashCode() ;
					
					if( ! newProds.containsKey( key ) )
					{	
						newProds.put( key , new_prod ) ;
						generateAllPossibleProds( new_prod ) ;
					}

					break ;
				}
		}
	}
	
	/**
	 * esta funcao permite obter os resultados obtidos apos todo o processamento
	 * da gramatica com epsilons. caso o processamento nao tenha sido efetuado 
	 * esta funcao retorna o mesmo vetor de producoes que recebeu na sua
	 * instanciacao
	 * @return Vector< Vector<String> > producoes
	 */
	public Vector<Vector<String>> getResults() {
		
		for( Vector<String> prod : productions ) 
			newProds.put( prod.hashCode() , prod ) ;
		
		productions.clear() ;
		
		for( Map.Entry<Integer,Vector<String> > entry : newProds.entrySet() ) 
			productions.add(  entry.getValue() ) ;
		
		cleanUpProdutions() ;
		
		return productions ;
	}
	
	/**
	 * funcao semelhante aquela que existe na classe ChomskyConverter
	 * para retirar producoes marcadas como desnecessarias
	 */
	private static void cleanUpProdutions()
	{
		Vector<Vector<String>> v = new Vector<Vector<String>>() ;
		for( Vector<String> thisprod : productions )
			if( ! thisprod.elementAt(0).equals(ERASE_ME) )
				v.add( thisprod ) ;
		
		productions = v ;
	}
	
	/**
	 * funcao para inserir os novos resultados no vetor de producoes para novos calculos
	 */
	private void addNewProductions() {
		
		productions = this.getResults() ;
	}

}