package interfaces;

public interface ICanOutputFormattedData {
	public void addCustomObjectFormatter(CustomObjectFormatter customObjectFormatter);
	public String format(Object object);
}
