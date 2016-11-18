package simplemath.math;

public class ReturnVal {
	private int errorCode;
	private int errorColumn;
	
	private double value;
	private boolean isNull = false;
	private String valueText = "";
	
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	public boolean isNull() {
		return isNull;
	}
	public void setNull(boolean isNull) {
		this.isNull = isNull;
	}
	
	public String getValueText() {
		return valueText;
	}
	public void setValueText(String valueStr) {
		this.valueText = valueStr;
	}
	public int getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	public int getErrorColumn() {
		return errorColumn;
	}
	public void setErrorColumn(int errorColumn) {
		this.errorColumn = errorColumn;
	}
}
