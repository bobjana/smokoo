package za.co.zynafin.smokoo.util.charts;

import static org.junit.Assert.*;

import org.junit.Test;

public class ChartUtilsTests {

	private static final Integer[][] int_data = new Integer[][]{
    {19,34,56},//1
    {5,5,2},//2
    {8,6,7},//3
    {34,4,5,8,2},//4
    {1},//5
    {34,5,2,1},//6
    {3,2,65},//7
	};
	
	private static final Double[][] double_set = new Double[][]{
      {50.0,1.20,16.34,6.2},//00-01
      {},//01-02
      {5.7,21.20,16.34,6.2},//03-04
      {2.7,21.20,16.34,6.2},//04-05
      {2.6,19.20,16.34,6.2},//05-06
      {1.45,1.20},//06-07
      {32.4,1.20,6.2},//07-08
      {32.4,1.20,6.2},//07-08
      {},//09-10
      {12.7,1.20,16.34,16.2},//10-11
      {18.2,1.20,16.34,16.2},//11-12
      {12.2,1.20,16.34,16.2},//12-13
      {13.2,1.20,16.34,16.2},//13-14
      {1.35,1.20,51.34,16.2},//14-15
      {17.75,10.20,2.34,16.2},//15-16
      {12.5,1.20,16.34,16.2},//16-17
      {1.8,1.20,4.34,16.2},//17-18
      {12.80,1.20,16.34,16.2},//18-19
      {12.1,1.20,3.34},//19-20
      {},//21-22
      {}//23-00
		};
	
	@Test
	public void getMin_int() throws Exception {
		ChartUtils<Integer> chartUtils = new ChartUtils<Integer>(Integer.class);
		Integer result = chartUtils.getMin(int_data);
		assertEquals(new Integer(1), result);
	}
	
	@Test
	public void getMax_int() throws Exception {
		ChartUtils<Integer> chartUtils = new ChartUtils<Integer>(Integer.class);
		Integer result = chartUtils.getMax(int_data);
		assertEquals(new Integer(65), result);
	}
	
	@Test
	public void getAvg_int() throws Exception {
		ChartUtils<Integer> chartUtils = new ChartUtils<Integer>(Integer.class);
		Integer result = chartUtils.getAvg(int_data);
		assertEquals(new Integer(28), result);
	}
	
	@Test
	public void getMin_double() throws Exception {
		ChartUtils<Double> chartUtils = new ChartUtils<Double>(Double.class);
		Double result = chartUtils.getMin(double_set);
		assertEquals(new Double(1.2), result);
	}
	
	@Test
	public void getMax_double() throws Exception {
		ChartUtils<Double> chartUtils = new ChartUtils<Double>(Double.class);
		Double result = chartUtils.getMax(double_set);
		assertEquals(new Double(51.34), result);
	}
	
	@Test
	public void getAvg_double() throws Exception {
		ChartUtils<Double> chartUtils = new ChartUtils<Double>(Double.class);
		Double result = chartUtils.getAvg(double_set);
		assertEquals(new Double(10.24), result);
	}
}
