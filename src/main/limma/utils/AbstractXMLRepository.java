package limma.utils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.basic.DateConverter;
import org.apache.commons.io.IOUtils;

public abstract class AbstractXMLRepository {
    private XStream xStream = new XStream();
    private File xmlFile;

    protected AbstractXMLRepository(File xmlFile) {
        this.xmlFile = xmlFile;
        DateConverter dateConverter = new DateConverter();
        xStream.registerConverter(dateConverter);
    }

    protected void addXmlAlias(String tagName, Class aClass) {
        xStream.alias(tagName, aClass);
    }

    protected void store(Object object) {
        backupXML();
        Writer writer = null;
        try {
            writer = new OutputStreamWriter(new GZIPOutputStream(new FileOutputStream(xmlFile)), "UTF-8");
            xStream.toXML(object, writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(writer);
        }
    }

    private void backupXML() {
        if (xmlFile.isFile()) {
            xmlFile.renameTo(new File(xmlFile.getAbsolutePath() + "_" + new SimpleDateFormat("yyyyMMdd_HHmm").format(new Date())));
        }
    }

    protected Object load() {
        if (!xmlFile.isFile() || xmlFile.length() == 0) {
            return null;
        }
        long start = System.currentTimeMillis();
        Reader reader = null;
        try {
            reader = new InputStreamReader(new GZIPInputStream(new FileInputStream(xmlFile)), "UTF-8");
            return xStream.fromXML(reader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(reader);
            System.out.println("Loaded " + xmlFile.getAbsolutePath() + " in " + (System.currentTimeMillis() - start) + " ms");
        }
    }
}
