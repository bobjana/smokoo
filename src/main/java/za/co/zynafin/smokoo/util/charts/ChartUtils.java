package za.co.zynafin.smokoo.util.charts;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class ChartUtils<T extends Number> {

	private Map<T[][],Number[]> cache = new HashMap<T[][],Number[]>();
	private Class<T> type;
	
	public ChartUtils(Class<T> type){
		this.type = type;
	}
	
	@SuppressWarnings("unchecked")
	public T getMin(T[][] data){
		if (!cache.containsKey(data)){
			cache.put(data, calculate(data));		
		}
		return (T)cache.get(data)[0];
	}

	@SuppressWarnings("unchecked")
	public T getMax(T[][] data){
		if (!cache.containsKey(data)){
			cache.put(data, calculate(data));		
		}
		return (T)cache.get(data)[1];
	}
	
	@SuppressWarnings("unchecked")
	public T getAvg(T[][] data){
		if (!cache.containsKey(data)){
			cache.put(data, calculate(data));		
		}
		if (type.equals(Integer.class)){
			return (T)new Integer(((Double)cache.get(data)[2]).intValue());
		}
		return (T)Double.valueOf(new DecimalFormat("#.##").format((T)cache.get(data)[2]));
	}

	private Number[] calculate(T[][] data) {
		Number avg = 0;
		Number min = 100000;
		Number max = 0;
		for (int i = 0; i < data.length;i++){
			for (int j = 0; j < data[i].length;j++){
				if (min.doubleValue() > getValue(data, i, j).doubleValue()){
					min = getValue(data, i, j);
				}
				if (max.doubleValue() < getValue(data, i, j).doubleValue()){
					max = getValue(data, i, j);
				}
				avg = avg.doubleValue() + getValue(data, i, j).doubleValue();
			}
			if (avg.doubleValue() > 0 && data[i].length > 0){
				avg = avg.doubleValue() / data[i].length;
			}
		}
		return new Number[]{min,max,avg};
	}

	private Number getValue(T[][] data, int i, int j) {
		return data[i][j];
	}
}
