package ru.retbansk.utils.marshaller;

import java.io.File;

public interface Marshaller {

	String marshal(Object object, File file);

}
