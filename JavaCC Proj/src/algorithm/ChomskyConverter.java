package algorithm;

import java.util.List;
import java.util.TreeMap;
import java.util.Vector;
import java.util.Map.Entry;

import common.Structure;


public class ChomskyConverter {

	private static final String ERASE_ME = "ERASE_ME";
	private static final int CFN_LIMIT = 4 ;
	private static int GEN_PRODUTION_NAME = 1 ;
	private static int GEN_VALUE_NAME = 1 ;
	
	private static Vector<Vector<String>> productions ;
	
	public ChomskyConverter( TreeMap<Structure, List<Vector<Structure>>> SymbolTable )
	{
		productions = new Vector<Vector<String>>();
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
	
	
	public void CNFconverter() {
		/*
		 * 1. Criar nova produção de START	( START0->START )
		 * 2. Eliminar eplsons
		 * 3. Remover produções únicas	( A->B )
		 * 4. Processar produções com 3 ou mais símbolos
		 * 5. Processar símbolos não terminais
		 * 6. DONE
		 */	
		printChomskyGrammar() ;
		
		// 1.
		newStartProduction() ;
		
		// 2.
		// gramática não aceita cadeias vazias!!!
		
		// 3.
		// a gramática não aceita A->B
		//removeUnitProductions() ;
		
		// 4.
		// a gramática não aceita A->BCD
		//processProductions() ;
		
		// 5.
		// a gramática não permite a existencia de A->Ab !!
		// processNonTerminals() ;
		
		printChomskyGrammar() ;
	}
	
	private static void newStartProduction() {
		Vector<String> startproduction = new Vector<String>() ;
		
		startproduction.add("START0") ;
		startproduction.add(":=") ;
		startproduction.add("START") ;
		
		Vector<Vector<String>> updatedproductions = new Vector<Vector<String>>() ;
		
		updatedproductions.add( startproduction ) ;
		for( Vector<String> prod : productions )
			updatedproductions.add( prod ) ;
		
		productions = updatedproductions ;
	}
	
	private void removeUnitProductions() 
	{	
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
							for( int i=2 ; i<sprod.size() ; i++ )
								newprod.add( sprod.elementAt(i) ) ;
							
							productions.add(newprod) ;
						}
					
					prod.clear() ; prod.add(ERASE_ME) ;
				}
			}
		}
		
		cleanUpProdutions() ;
	}
	
	private void processProductions() 
	{
		for( Vector<String> prod : productions )
		{
			if( prod.size() > CFN_LIMIT )
			{
				Vector<String> v1 = new Vector<String>() ;
				String chomskyp = genProdName() ;
				v1.add( prod.elementAt(0) ) ;
				v1.add( prod.elementAt(1) ) ;
				v1.add( prod.elementAt(2) ) ;
				v1.add( chomskyp ) ;
				
				Vector<String> v2 = new Vector<String>() ;
				v2.add( chomskyp ) ;
				v2.add( ":=") ;
				for( int i=3 ; i<prod.size() ; i++ )
					v2.add( prod.elementAt(i) ) ;
				
				productions.add(v1) ;
				productions.add(v2) ;
				
				prod.clear() ; prod.add(ERASE_ME) ;
			}
		}
		
		cleanUpProdutions() ;
	}
	
	private void processNonTerminals()
	{
		for( Vector<String> prod : productions ) // para todos os elementos
		{
			// é suposto neste momento todas as produções
			// serem do género A->a ou então A->AB
			
			// existem 3 situações: A->aB	A->Ba	A->aa
			if( prod.size() == 4 ) 
			{
				byte b1 = prod.elementAt(2).getBytes()[0] ;
				byte b2 = prod.elementAt(3).getBytes()[0] ;
				
				String s1 , s2 ;
				if( b1>='a' && b1<='z' ) // primeiro elemento
				{
					s1 = genValName() ;
					Vector<String> genVal1 = new Vector<String>() ;
					genVal1.add( s1 ) ;
					genVal1.add(":=") ;
					genVal1.add( prod.elementAt(2) ) ;
					productions.add( genVal1 ) ;
				}
				else
					s1 = prod.elementAt(2) ;
				
				if( b2>='a' && b2<='z' ) // segundo elemento
				{
					s2 = genValName() ;
					Vector<String> genVal2 = new Vector<String>() ;
					genVal2.add( s2 ) ;
					genVal2.add(":=") ;
					genVal2.add( prod.elementAt(3) ) ;
					productions.add( genVal2 ) ;
				}
				else
					s2 = prod.elementAt(3) ;
				
				Vector<String> v = new Vector<String>() ;
				v.add( prod.elementAt(0) ) ;
				v.add( prod.elementAt(1) ) ;
				v.add( s1 ) ;
				v.add( s2 ) ;
				productions.add( v ) ;
				
				prod.clear() ; prod.add(ERASE_ME) ;
			}
		}
		
		cleanUpProdutions() ;
	}
	
	
	
	/**
	 *  generate a sring for a new terminal symbol
	 * @return
	 */
	private static String genValName()
	{
		String s = new String("ChomskyVal") ;
		return s+GEN_VALUE_NAME ;
	}
	
	/**
	 *  generate a string for a new non-terminal symbol
	 * @return
	 */
	private static String genProdName()
	{
		String s = new String("ChomskyProd") ;
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
}
