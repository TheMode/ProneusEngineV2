package fr.proneus.engine.data;

public class Data<T> {

	private Object object;

	protected Data(Object object) {
		this.object = object;
	}

	@SuppressWarnings("unchecked")
	public T get() {
		return (T) object;

	}

}
