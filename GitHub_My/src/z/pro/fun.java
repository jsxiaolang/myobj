/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package z.pro;

import java.io.File;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;

/**
 *
 * @author Administrator
 */
public class fun {

    public static boolean isOnline() {
        boolean bs = false;
        URL url = null;
        try {
            url = new URL("http://www.baidu.com/");
            InputStream in = url.openStream();
            bs = true;
            in.close();
        } catch (Exception e) {

        }
        return bs;
    }
}
