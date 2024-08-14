package br.com.erick.ac.model3D;

import java.math.BigInteger;
import java.security.InvalidParameterException;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import br.com.erick.ac.Cell;

/**
 *  Automato Celular tridimensional binário, isto é, um cubo de bits de dois estados
 *  que varia conforme uma regra construida a partir de dois longs, ou seja, a regra de
 *  transição é um número de 128 bits (2 longs), o que dá 2^128 regras possíveis (incluído regras
 *  que geram extinção, superpopulação ou estagnação).
 *  
 *  
 *  @author Erick Andrade
 */

public class CellularAutomata3D {
	
	/**
	 * Tamanho recomendado para gerar um cubo 8x8x8, uma vez que a implementação
	 * ignora as bordas do cubo, e esse tamanho gera uma string de 512 bits,
	 * o que é interessante para trabalho com números grandes e é potência de 2,
	 * interessante para trabalhar com números binários.
	 */
	
	public static final int RECOMENDED_SIZE = 10;
	
	private static final Long[] SEED_GENERATION_RULE = {
			6325471989652881993l, 7954789654479784523l
	};
	
	private Cell[][][] cube;
	private String rule;
	private int size;
	
	/**
	 * 
	 * Constrói um automato celular tridimensional, nessa implementação,
	 * um cubo de bits que muta conforme a aplicação de uma regra
	 * 
	 * @param size largura do cubo
	 * @param r1 primeiro long da regra
	 * @param r2 segundo long da regra
	 */
	
	public CellularAutomata3D(int size, long r1, long r2) {
		setRule(r1, r2);
		this.size = size;
		initCube(size);
	}
	
	/**
	 * 
	 * Constrói um automato celular tridimensional, nessa implementação,
	 * um cubo de bits que muta conforme a aplicação de uma regra. Lado do
	 * cubo é de 10x10x10 (Ignorando as bordas: 8x8x8)
	 * 
	 * @param Regra de transição
	 */
	
	public CellularAutomata3D(Long[] rule) {
		setRule(rule[0], rule[1]);
		this.size = RECOMENDED_SIZE;
		initCube(size);
	}
	
	/**
	 * 
	 * Constrói um automato celular tridimensional, nessa implementação,
	 * um cubo de bits que muta conforme a aplicação de uma regra
	 * 
	 * @param size largura do cubo
	 * @param rule - array de 2 posições que representa a regra do autômato
	 */
	
	public CellularAutomata3D(int size, Long[] rule) {
		setRule(rule[0], rule[1]);
		this.size = size;
		initCube(size);
	}
	
	/**
	 * 
	 * @return String que representa a regra definida no construtor, em forma binária.
	 */
	
	public String getRule() {
		return new StringBuilder(this.rule).reverse().toString();
	}
	
	private void setRule(long r1, long r2) {
		String num1 = Long.toBinaryString(r1);
		String num2 = Long.toBinaryString(r2);
		String num3 = num1 + num2;
		while(num3.length() < 128) {
			num3 = "0" + num3;
		}
		this.rule = new StringBuilder(num3).reverse().toString();
	}
	
	private void initCube(int size) {
		this.cube = new Cell[size][size][size];
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				for(int k = 0; k < size; k++) {
					cube[i][j][k] = new Cell(this.rule);					
				}
			}
		}
	}
	
	/**
	 * Aplica a regra de transição no cubo, alterando os estados dos bits
	 * conforme a regra passada no construtor da classe
	 */
	
	public void nextGen() {
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				for (int k = 0; k < size; k++) {
					try {
						String rlm = "" + cube[i - 1][j][k].getPreviousStateString()
								+ cube[i][j - 1][k].getPreviousStateString() +
								cube[i][j][k - 1].getPreviousStateString() +
								cube[i][j][k] +
								cube[i][j][k + 1] +
								cube[i][j + 1][k] +
								cube[i + 1][j][k];
						cube[i][j][k].applyRule(rlm);
					} catch (ArrayIndexOutOfBoundsException e) {
						continue;
					} 
				}
			}
		}
	}
	
	/**
	 * 
	 * @return uma String que representa o estado atual do cubo,
	 *  separado por "fatias"
	 */
	
	public String getFormatedFrame() {
		String frame = "";
		for(int i = 1; i < size - 1; i++) {
			for(int j = 1; j < size - 1; j++){
				for (int k = 1; k < size - 1; k++) {
					frame += cube[i][j][k] + " ";
				}
				frame += "\n";
			}
			frame += "\n";
		}
		return frame;
	}
	
	/**
	 * 
	 * @return uma String que representa o estado atual do cubo, de forma linear
	 */
	
	public String getFrame() {
		String frame = "";
		for(int i = 1; i < size - 1; i++) {
			for(int j = 1; j < size - 1; j++){
				for (int k = 1; k < size - 1; k++) {
					frame += cube[i][j][k];
				}
			}
		}
		return frame;
	}
	
	/**
	 * Seta o bit no meio do cubo como 1 para dar início ao funcionamento do automato.
	 */
	
	public void setDefaultInitialPoint() {
		cube[size/2][size/2][size/2].setCurrentState(true);
	}
	
	/**
	 * Seta um bit do cubo como 1, na cordenada passada nos parâmetros da função
	 * @param x coordenada x do cubo
	 * @param y coordenada y do cubo
	 * @param z coordenada z do cubo
	 */
	public void setInitialPoint(int x, int y, int z) {
		cube[x][y][z].setCurrentState(true);
	}
	
	/**
	 * Testa uma regra, transicionando o automato 10000 vezes.
	 * Se em alguma vez ele gerar um numero repetido, retorna false.
	 * Senão retorna verdadeiro, indicando que o conjunto de regras
	 * é relativamente bom. 
	 * 
	 * @param r1 o primeiro long da regra
	 * @param r2 o segundo long da regra
	 * 
	 * @return retorna true se a semente não gera números repetidos
	 * 
	 * */
	
	public static boolean fastSeedTest(long r1, long r2) {
		CellularAutomata3D ac = new CellularAutomata3D(RECOMENDED_SIZE, r1, r2);
		Set<BigInteger> list = new HashSet<>();
		ac.setDefaultInitialPoint();
		for(int i = 0; i < 1000; i++) {
			ac.nextGen();
			BigInteger bg = new BigInteger(ac.getFrame(), 2);
			if(list.contains(bg)) return false;
			list.add(bg);
		}
		return true;
	}
	
	/**
	 * Testa uma regra, transicionando o automato 100000 vezes.
	 * Se em alguma vez ele gerar um numero repetido, retorna false.
	 * Senão retorna verdadeiro, indicando que o conjunto de regras
	 * é bom. Itera 100 vezes mais que o fastSeedTest(), dando assim,
	 * uma segurança maior de que a semente não gera números repetidos.
	 * Demora muito mais para retornar o resultado do teste
	 * 
	 * @param r1 o primeiro long da regra
	 * @param r2 o segundo long da regra
	 * 
	 * @return retorna true se a semente não gera números repetidos
	 * 
	 * */
	
	public static boolean goodSeedTest(long r1, long r2) {
		CellularAutomata3D ac = new CellularAutomata3D(RECOMENDED_SIZE, r1, r2);
		Set<BigInteger> list = new HashSet<>();
		ac.setDefaultInitialPoint();
		for(int i = 0; i < 100000; i++) {
			ac.nextGen();
			BigInteger bg = new BigInteger(ac.getFrame(), 2);
			if(list.contains(bg)) return false;
			list.add(bg);
		}
		return true;
	}
	
	private static Long[] getPseudoRandomRuleGenerator() {
		CellularAutomata3D ac = new CellularAutomata3D(RECOMENDED_SIZE, SEED_GENERATION_RULE[0], SEED_GENERATION_RULE[1]);
		Integer nano = Instant.now().getNano();
		String nanoSlice = nano.toString().substring(0, 3);
		int num = Integer.parseInt(nanoSlice);
		ac.setDefaultInitialPoint();
		for(int i = 0; i < num; i++) {
			ac.nextGen();
		}
		String frame = ac.getFrame();
		String rule1 = frame.substring(0, 63);
		String rule2 = frame.substring(63, 126);
		Long[] rules = {Long.parseLong(rule1, 2), Long.parseLong(rule2, 2)};
		while(!fastSeedTest(rules[0], rules[1])){
			rules = getPseudoRandomRuleGenerator();
		}
		return rules;
	}
	
	/**
	 * 
	 * @return um array com regra para construção de um autômato 3d
	 */
	
	public static Long[] getRandomRule() {
		return getRandomRule(1);
	}
	
	private static Long[] getRandomRule(int recursionValue) {
		if(recursionValue < 0) {
			throw new InvalidParameterException("O valor da recursão não pode ser menor que 0");
		}
		Long[] ranndomRule;
		if(recursionValue == 0) {			
			ranndomRule = getPseudoRandomRuleGenerator();
		}
		else {
			ranndomRule = getRandomRule(recursionValue - 1);
		}
		CellularAutomata3D ac = new CellularAutomata3D(RECOMENDED_SIZE, ranndomRule[0], ranndomRule[1]);
		Integer nano = Instant.now().getNano();
		String nanoSlice = nano.toString().substring(0, 3);
		int num = Integer.parseInt(nanoSlice);
		ac.setDefaultInitialPoint();
		for(int i = 0; i < num; i++) {
			ac.nextGen();
		}
		String frame = ac.getFrame();
		String rule1 = frame.substring(0, 63);
		String rule2 = frame.substring(63, 126);
		Long[] rules = {Long.parseLong(rule1, 2), Long.parseLong(rule2, 2)};
		while(!fastSeedTest(rules[0], rules[1])){
			rules = getRandomRule(0);
		}
		return rules;
	}
	
	/**
	 * @return retorna um inteiro aleatório gerado com base em um regra gerada pseudoaleatoriamente.
	 * Rápido, porém tem maior chance de repetição
	 */
	
	public static int fastRandomInt() {
		Long[] rules = getPseudoRandomRuleGenerator();
		CellularAutomata3D ac = new CellularAutomata3D(RECOMENDED_SIZE, rules[0], rules[1]);
		Integer iterations = Instant.now().getNano();
		int num = Integer.parseInt(iterations.toString().substring(0, 3));
		ac.setDefaultInitialPoint();
		for(int i = 0; i < num; i++) {
			ac.nextGen();
		}
		String ff = ac.getFrame();
		String fs = "00000000000000000000000000000000";
		for(int i = 0; i < 512; i += 32) {
			fs = applyXOR(fs, ff.substring(i, i + 32));
		}
		return Integer.parseInt(fs.substring(1), 2) == 0 ? fastRandomInt() : Integer.parseInt(fs.substring(1), 2);
	}
	
	/**
	 * 
	 * @return retorna um inteiro aleatório gerado com base em um regra gerada aleatoriamente.
	 * Mais lento que o fastRandomInt(), porém, com menos chance de repetição
	 */
	
	public static int goodRandomInt() {
		Long[] rules = getRandomRule(0);
		CellularAutomata3D ac = new CellularAutomata3D(RECOMENDED_SIZE, rules[0], rules[1]);
		Integer iterations = Instant.now().getNano();
		int num = Integer.parseInt(iterations.toString().substring(0, 3));
		ac.setDefaultInitialPoint();
		for(int i = 0; i < num; i++) {
			ac.nextGen();
		}
		String ff = ac.getFrame();
		String fs = "00000000000000000000000000000000";
		for(int i = 0; i < 512; i += 32) {
			fs = applyXOR(fs, ff.substring(i, i + 32));
		}
		return Integer.parseInt(fs.substring(1), 2) == 0 ? goodRandomInt() : Integer.parseInt(fs.substring(1), 2);
	}
	
	/**
	 * 
	 * @return retorna um inteiro aleatório gerado com base em um regra gerada aleatoriamente.
	 * Mais lento que o goodRandomInt(), porém, com menos chance de repetição
	 */
	
	public static int greatRandomInt() {
		Long[] rules = getRandomRule(1);
		CellularAutomata3D ac = new CellularAutomata3D(RECOMENDED_SIZE, rules[0], rules[1]);
		Integer iterations = Instant.now().getNano();
		int num = Integer.parseInt(iterations.toString().substring(0, 3));
		ac.setDefaultInitialPoint();
		for(int i = 0; i < num; i++) {
			ac.nextGen();
		}
		String ff = ac.getFrame();
		String fs = "00000000000000000000000000000000";
		for(int i = 0; i < 512; i += 32) {
			fs = applyXOR(fs, ff.substring(i, i + 32));
		}
		return Integer.parseInt(fs.substring(1), 2) == 0 ? greatRandomInt() : Integer.parseInt(fs.substring(1), 2);
	}
	
	/**
	 * @param recursionValue - valor que determina o grau de recursão do algoritmo de obtenção da regra do autômato
	 * usado para gerar o número aleatório. Exemplo: se 0 esse método tera o mesmo grau de eficiência que o goodRandomInt()
	 * , se 1, igual ao greatRandomInt(), o perfectRandomInt() usa o valor 5 de recursão.
	 * 
	 * @return retorna um inteiro aleatório gerado com base em um regra gerada aleatoriamente.
	 * Mais lento que o fastRandomInt(), porém, com menos chance de repetição
	 */
	
	public static int getRandomInt(int recursionValue) {
		Long[] rules = getRandomRule(recursionValue);
		CellularAutomata3D ac = new CellularAutomata3D(RECOMENDED_SIZE, rules[0], rules[1]);
		Integer iterations = Instant.now().getNano();
		int num = Integer.parseInt(iterations.toString().substring(0, 3));
		ac.setDefaultInitialPoint();
		for(int i = 0; i < num; i++) {
			ac.nextGen();
		}
		String ff = ac.getFrame();
		String fs = "00000000000000000000000000000000";
		for(int i = 0; i < 512; i += 32) {
			fs = applyXOR(fs, ff.substring(i, i + 32));
		}
		return Integer.parseInt(fs.substring(1), 2) == 0 ? fastRandomInt() : Integer.parseInt(fs.substring(1), 2);
	}
	
	/**
	 * Bom método para obtenção de números aleatórios, porém, os mais lento entre todos. Utiliza 5 níveis de recursão,
	 * além utilizar o goodtSeedTest() para chegagem da semente gerada. Muito lento, somente utilizado para fins de teste
	 * 
	 * @return inteiro gerado aleatoriamente
	 */
	
	public static int perfectRandomInt() {
		Long[] rules = getRandomRule(5);
		while(!goodSeedTest(rules[0], rules[1])){
			rules = getRandomRule(5);
		}
		CellularAutomata3D ac = new CellularAutomata3D(RECOMENDED_SIZE, rules[0], rules[1]);
		Integer iterations = Instant.now().getNano();
		int num = Integer.parseInt(iterations.toString().substring(0, 5));
		ac.setDefaultInitialPoint();
		for(int i = 0; i < num; i++) {
			ac.nextGen();
		}
		String ff = ac.getFrame();
		String fs = "00000000000000000000000000000000";
		for(int i = 0; i < 512; i += 32) {
			fs = applyXOR(fs, ff.substring(i, i + 32));
		}
		return Integer.parseInt(fs.substring(1), 2);
	}
	
	public static BigInteger getRandomBigInteger() {
		Long[] rules = getRandomRule(0);
		while(!fastSeedTest(rules[0], rules[1])){
			rules = getRandomRule(0);
		}
		CellularAutomata3D ac = new CellularAutomata3D(RECOMENDED_SIZE, rules);
		Integer iterations = Instant.now().getNano();
		int num = Integer.parseInt(iterations.toString().substring(0, 3));
		ac.setDefaultInitialPoint();
		for(int i = 0; i < num; i++) {
			ac.nextGen();
		}
		return new BigInteger(ac.getFrame(), 2);
	}
	
	private static String applyXOR(String arg0, String arg1) {
		if(arg0.length() != arg1.length()) throw new RuntimeException("Strings de tamanhos diferentes");
		String result = "";
		for(int i = 0; i < arg0.length(); i++) {
			if((arg0.charAt(i) == '0' && arg1.charAt(i) == '1') || (arg1.charAt(i) == '0' && arg0.charAt(i) == '1')) {
				result += "1";
			}else {
				result += "0";
			}
		}
		return result;
	}
}