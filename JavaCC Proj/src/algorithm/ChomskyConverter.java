package algorithm;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.TreeMap;
import java.util.Vector;
import java.util.Map.Entry;

import common.Structure;

public class ChomskyConverter {

	private static final String ERASE_ME = "ERASE_ME";
	private static final String START0 = "START0" ;
	private static final String START = "START" ;
	private static final String OLDSTART = "OLDSTART" ;
	private static final int CFN_LIMIT = 4 ;
	private static int GEN_PRODUTION_NAME = 0 ;
	private static int GEN_VALUE_NAME = 0 ;
	
	private boolean analised ; // indica se a gramática já foi validada
	private boolean isCNF ; // indica se a gramatica está na forma CNF
	
	private static Vector<Vector<String>> productions ;
	
 	public ChomskyConverter( TreeMap<Structure, List<Vector<Structure>>> SymbolTable )
	{
		productions = new Vector<Vector<String>>();
		analised = false ;
		isCNF = false ;
		
		for(Entry<Structure, List<Vector<Structure>>> entry : SymbolTable.entrySet()) {
			List<Vector<Structure>> value = entry.getValue();
			Structure key = entry.getKey();
			for (Vector<Structure> i : value) {
				Vector<String> toAdd = new Vector<String>();
				toAdd.add(key.name);
				toAdd.add(":=");
				for(Structure x:i)
				{
					toAdd.add(x.name);
				}
				productions.add(toAdd);
			}
		}
	}
	
 	/**
 	 * Conversor da gramática CFG para a forma CNF
 	 * @return
 	 */
	public Vector<Vector<String>> CNFconverter() {
		/*
		 * 1. Criar nova produção de START	( START0->START )
		 * 2. Eliminar eplsons
		 * 3. Remover produções únicas	( A->B )
		 * 4. Processar produções com 3 ou mais símbolos
		 * 5. Processar símbolos terminais
		 * 6. DONE
		 */	

		if( ! this.analised ) // antes de converter, a validação é obrigatória!!
			isCNF() ;
		
		if( this.isCNF ) // caso já esteja na forma normal devolve o vetor de produções inicial
			return productions ;
		
		
		System.out.println("\nConverting grammar to Chomsky Normal Form...") ;
		
		// 1.
		newStartProduction() ;
		// 2.
		// gramática não aceita cadeias vazias!!!
		// 3.
		removeUnitProductions() ;
		// 4.
		processProductions() ;
		// 5.
		processTerminals() ;
		
		corretGrammar() ;	
		
		
		System.out.println("Conversiton completed! This is the result.\n") ;
		
		//saveProductions() ;
		organizeFile() ;
		printChomskyGrammar() ;
		
		System.out.println("---------------------------------------------------\n") ;
		
		return productions ;
	}
	
	/**
	 * Primeiro passo da conversão: criar nova produção de START
	 */
	private static void newStartProduction() {
		Vector<String> startproduction = new Vector<String>() ;
		
		startproduction.add(START0) ;
		startproduction.add(":=") ;
		startproduction.add(START) ;
		
		Vector<Vector<String>> updatedproductions = new Vector<Vector<String>>() ;
		
		updatedproductions.add( startproduction ) ;
		for( Vector<String> prod : productions )
			updatedproductions.add( prod ) ;
		
		productions = updatedproductions ;
	}
	
	/**
	 * Segundo passo da conversão: remover produções únitárias
	 */
	private static void removeUnitProductions() 
	{	
		Vector<Vector<String>> temp_prod = new Vector<Vector<String>>() ;
		boolean doThisAgain = false ;
		
		do {
			doThisAgain = false ;
			
			for( Vector<String> prod : productions ) // para todos os elementos
			{
				if( prod.size() == 3 ) // procurar aqueles com produções unitárias
				{
					String searchProd = new String( prod.elementAt(2) ) ; // anotar produção
					
					// só interessam produções do tipo A->B e não A->b !!
					if( ! (searchProd.getBytes()[0] >= 'a' && searchProd.getBytes()[0] <= 'z') )
					{
						for( Vector<String> sprod : productions ) // procurar em todos
							if( sprod.elementAt(0).equals(searchProd)) // a produção anotada
							{
								Vector<String> newprod = new Vector<String>() ;
								newprod.add( prod.elementAt(0) ) ;
								newprod.add( ":=" ) ;
								
								int i=2 ;
								for(; i<sprod.size() ; i++ )
									newprod.add( sprod.elementAt(i) ) ;
								
								if( i==3 && sprod.elementAt(i-1).getBytes()[0]>=65 && sprod.elementAt(i-1).getBytes()[0]<=90 )
									doThisAgain=true ;
								
								temp_prod.add(newprod) ;
							}
						
						prod.clear() ; prod.add(ERASE_ME) ;
					}
				}
			}
			
			for( Vector<String> v : temp_prod )
				productions.add(v) ;
			
			temp_prod.clear() ;
			cleanUpProdutions() ;
			
		} while( doThisAgain ) ;
		
	}
	
	/**
	 * Terceiro passo da conversão: devidir produções com 3 ou mais símbolos
	 */
	private static void processProductions() 
	{
		Vector<Vector<String>> temp_prod = new Vector<Vector<String>>() ;
		
		for( Vector<String> prod : productions )
		{
			if( prod.size() > CFN_LIMIT )
			{
				Vector<String> v1 = new Vector<String>() ;
				
				String chomskyp = new String() ;		
				
				boolean found = false ;
				
				for( Vector<String> v : temp_prod )
				{
					found = false ;
					
					if( v.size() == prod.size()-1 )
					{
						for( int i=2 ; i<v.size() ; i++)
						{
							if( v.elementAt(i).equals( prod.elementAt(i+1) ) )
							{
								
								found = true ;
							}
							else
							{
								found = false ;
								break ;
							}
						}
					}
					
					if ( found )
					{
						chomskyp = v.elementAt(0) ;
						break ;
					}
				} 
					
				if( ! found )
					chomskyp = genProdName() ;
				
				v1.add( prod.elementAt(0) ) ;
				v1.add( prod.elementAt(1) ) ;
				v1.add( prod.elementAt(2) ) ;
				v1.add( chomskyp ) ;
				
				if ( ! found )
				{
					Vector<String> v2 = new Vector<String>() ;
					v2.add( chomskyp ) ;
					v2.add( ":=") ;
					for( int i=3 ; i<prod.size() ; i++ )
						v2.add( prod.elementAt(i) ) ;
					
					temp_prod.add(v2) ;
				}
				
				temp_prod.add(v1) ;
				
				prod.clear() ; prod.add(ERASE_ME) ;
			}
		}
		
		for( Vector<String> v : temp_prod )
			productions.add(v) ;
		
		cleanUpProdutions() ;
	}
	
	/**
	 * Quarto passo da conversão: processar símbolos terminais
	 */
	private static void processTerminals()
	{
		Vector<Vector<String>> temp_prod = new Vector<Vector<String>>() ;
		
		for( Vector<String> prod : productions ) // para todos os elementos
		{
			// é suposto neste momento todas as produções
			// serem do género A->a ou então A->AB
			
			// existem 3 situações: A->aB	A->Ba	A->aa
			if( prod.size() == 4 ) 
			{
				byte b1 = prod.elementAt(2).getBytes()[0] ;
				byte b2 = prod.elementAt(3).getBytes()[0] ;
				
				String s1 = new String() ;
				String s2 = new String() ;
				
				if( b1>='a' && b1<='z' ) // primeiro elemento
				{
					boolean found = false ;
					
					for( Vector<String> v : temp_prod )
						if( v.elementAt(2).equals(prod.elementAt(2)) )
						{
							s1 = v.elementAt(0) ;
							found = true ;
						}
					
					if( ! found )
						s1 = genValName() ;
					
					Vector<String> genVal1 = new Vector<String>() ;
					genVal1.add( s1 ) ;
					genVal1.add(":=") ;
					genVal1.add( prod.elementAt(2) ) ;
					
					if( ! found )
						temp_prod.add( genVal1 ) ;
				}
				else
					s1 = prod.elementAt(2) ;
				
				if( b2>='a' && b2<='z' ) // segundo elemento
				{
					boolean found = false ;
					
					for( Vector<String> v : temp_prod )
						if( v.elementAt(2).equals(prod.elementAt(3)) )
						{
							s2 = v.elementAt(0) ;
							found = true ;
						}

					if( ! found )
						s2 = genValName() ;
					
					Vector<String> genVal2 = new Vector<String>() ;
					genVal2.add( s2 ) ;
					genVal2.add(":=") ;
					genVal2.add( prod.elementAt(3) ) ;
					
					if( ! found )
						temp_prod.add( genVal2 ) ;
				}
				else
					s2 = prod.elementAt(3) ;
				
				Vector<String> v = new Vector<String>() ;
				v.add( prod.elementAt(0) ) ;
				v.add( prod.elementAt(1) ) ;
				v.add( s1 ) ;
				v.add( s2 ) ;
				temp_prod.add( v ) ;
				
				prod.clear() ; prod.add(ERASE_ME) ;
			}
		}
		
		for( Vector<String> v : temp_prod )
			productions.add(v) ;
		
		cleanUpProdutions() ;
	}

	/**
	 * verifica se a gramática está na forma normal de chomsky
	 * @return true or false
	 */
	public boolean isCNF() {
		
		// 1. verify no loop on Start
		// 2. verify P -> P1 P2
		// 3. verify P -> p
		
		if ( this.analised ) // the second time this method is called it return the first result
			return this.isCNF ;
		
		System.out.println("\n------------ Chomsky Normal Form Validator ------------") ;
		System.out.println("Testing if grammar is in Chomsky Normal Form...\n") ;
		
		this.analised = true ;
		this.isCNF = true ;
		
		for( Vector<String> prod : productions )
		{
			if ( prod.size() > 4 )
			{
				printValidationError(prod,1) ;
				this.isCNF = false ;
				//return false ; // nao respeita a regra 2, significa que a produção é do tipo P->ABC, p.e.
			}
			else if ( prod.size() == 4 )
			{
				if ( prod.elementAt(2).equals(ChomskyConverter.START) || 
						prod.elementAt(3).equals(ChomskyConverter.START) )
				{
					printValidationError(prod,2) ;
					this.isCNF = false ;
					//return false ; // nao respeita a regra 1, há um loop para o start
				}
				
				byte p1 = prod.elementAt(2).getBytes()[0] ;
				byte p2 = prod.elementAt(3).getBytes()[0] ;
				
				// A:65  a  Z:90  		ASCII codes
				if( ! (p1>=65 && p1<=90 && p2>=65 && p2<=90) )
				{
					printValidationError(prod,3) ;
					this.isCNF = false ;
					//return false ; // nao respeita a regra 2, significa que a produção é do tipo P->aB ou P->aa
				}
			}
			else if( prod.size() == 3 )
			{
				byte p1 = prod.elementAt(2).getBytes()[0] ;
				
				// a:97  a  z:122		ASCII codes
				if( ! (p1>=97 && p1<=122) )
				{
					printValidationError(prod,4) ;
					this.isCNF = false ;
					//return false ; // nao respeita a regra 1, significa que a produção é do tipo P->A
				}
			}
			else
			{
				printValidationError(prod,5) ;
				this.isCNF = false ;
				//return false ; // nunca chega aqui porque não são aceites produções para o vazio pela gramática
			}
		}
		
		if( this.isCNF ) {
			System.out.println("> [Valid] There's no need for conversitions.") ;
			System.out.println("---------------------------------------------------\n") ;
		}
		
		return this.isCNF ;
	}
	
	/**
	 * grava a gramatica num ficheiro de texto de nome 'output.txt'
	 */
	private static void creatNewFile() {
		
		try
		{	
			File f = new File( "CNFoutput.txt" ) ;
			if ( f.exists() )
				f.delete() ;
			
			f.createNewFile() ;
			
		    FileWriter fw = new FileWriter("CNFoutput.txt",true) ;
		    
		    for( Vector<String> v : productions )
		    {
		    	for( String s : v )
		    	{
		    		fw.write( s + " " ) ; 
		    	}
		    	fw.write(";\n") ;
		    }
		    
		    fw.write("END") ;
		    
		    fw.close();
		}
		catch(IOException ioe)	{ ioe.printStackTrace(); }
	}
	
	/**
	 * organiza as produções do vetor 'productions'
	 */
	private static void organizeFile() {
		
		Vector<Vector<String>> aux = new Vector<Vector<String>>() ;
		
		// primeiros 'START'
		for( Vector<String> v : productions )
			if( v.elementAt(0).equals(ChomskyConverter.START) )
				aux.add(v) ;
		// depois os antigos 'START'
		
		for( Vector<String> v : productions )
			if( v.elementAt(0).equals(ChomskyConverter.OLDSTART) )
				aux.add(v) ;
		// as restantes produções
		
		for( Vector<String> v : productions )
			if( v.size() == 4 && !v.elementAt(0).equals(ChomskyConverter.OLDSTART) 
			&& !v.elementAt(0).equals(ChomskyConverter.START))
				aux.add(v) ;
		
		// e finalmente os terminais
		for( Vector<String> v : productions )
			if( v.size() < 4 && !v.elementAt(0).equals(ChomskyConverter.OLDSTART) 
					&& !v.elementAt(0).equals(ChomskyConverter.START) )
				aux.add(v) ;
		
		productions = aux ;
	}
	
	/**
	 * organiza e grava as produções da gramática num ficheiro de texto 'output.txt'
	 */
	@SuppressWarnings("unused")
	private static void saveProductions() {
		organizeFile() ;
		creatNewFile() ;
	}
	
	/**
	 * corrige o nome da produção inicial 'START' para ser aceite pela gramática do parser
	 */
	private static void corretGrammar() {
		
		Vector<Vector<String>> aux1 = new Vector<Vector<String>>() ;
		
		for( Vector<String> v : productions )
		{
			Vector<String> aux_v = new Vector<String>() ;
			
			for( String s : v )
			{				
				if( s.equals(START) )
					aux_v.add(OLDSTART) ;
				else
					aux_v.add( s ) ;
			}
			
			aux1.add( aux_v ) ;
		}
		
		productions = aux1 ;
		
		Vector<Vector<String>> aux2 = new Vector<Vector<String>>() ;

		for( Vector<String> v : aux1 )
		{
			if ( v.elementAt(0).equals(START0) )
			{
				Vector<String> aux_v = new Vector<String>() ;
				aux_v.add(START) ;
				for( int i=1 ; i<v.size() ; i++ )
					aux_v.add( v.elementAt(i) ) ;
				
				aux2.add( aux_v ) ;
			}
			else
				aux2.add(v) ;
		}
		
		productions = aux2 ;
	}
	
	/**
	 *  generate a sring for a new terminal symbol
	 * @return
	 */
	private static String genValName()
	{
		String s = new String("ValC") ;
		GEN_VALUE_NAME+=1;
		
		return s+GEN_VALUE_NAME ;
	}
	
	/**
	 *  generate a string for a new non-terminal symbol
	 * @return
	 */
	private static String genProdName()
	{
		String s = new String("ProdC") ;
		GEN_PRODUTION_NAME+=1;
		
		return s+GEN_PRODUTION_NAME ;
	}
	
	/**
	 * erase the elements not needed
	 */
	private static void cleanUpProdutions()
	{
		// elimina produções descartadas
		Vector<Vector<String>> v = new Vector<Vector<String>>() ;
		for( Vector<String> thisprod : productions )
			if( ! thisprod.elementAt(0).equals(ERASE_ME) )
				v.add( thisprod ) ;
		
		productions = v ;
	}

	/**
	 * prints the converted grammar on the console
	 */
	public static void printChomskyGrammar()
	{
		System.out.println("------------ Chomsky Normal Form ------------") ;
		for( Vector<String> prod : productions )
		{
			System.out.print(">   ") ;
			for( String s : prod )
				System.out.print( s + " " ) ;
			
			System.out.println() ;
		}
		System.out.println("---------------------------------------------") ;
	}

	/**
	 * prints to the console the invalid production found on the grammar. 
	 * In case the error isn't known, this function won't print anything
	 * @param prod : is the invalid CNF production founded
	 * @param error : type of error; type of invalid production
	 * error = { 1 ; 2 ; 3 ; 4 ; 5 }
	 * 		error = 1	, in case the productions have too many symbols, P->ABC, etc
	 * 		error = 2	, in case there is a loop on START production
	 * 		error = 3	, in case there is a production with TERM and NONTERM like P->aB or TERMS like P->aa
	 * 		error = 4	, in case there is an unitary production like P->A
	 * 		error = 5	, in case there is a production to epsilon
	 */
	private void printValidationError(Vector<String>prod , int error ) {
		
		switch(error){
		case 1:
			System.out.println("> [Invalid] Found a production which exceded number of symbols.") ;
			break;
		case 2:
			System.out.println("> [Invalid] There is a loop on START.") ;
			break;
		case 3:
			System.out.println("> [Invalid] Found a production that violates rule P->P1 P2.") ;
			break;
		case 4:
			System.out.println("> [Invalid] Found a production that violates rule P->a.") ;
			break;
		case 5:
			System.out.println("> [Invalid] Found a production to epsilon.") ;
			break;
		default:
			return ;
		}
		
		System.out.print("\t Invalid production: " );
		for( int i=0 ; i<prod.size(); i++)
			System.out.print( prod.elementAt(i) + " " ) ;
		System.out.println("\n") ;		
	}
}
