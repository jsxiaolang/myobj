/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.GetFile;

import util.AbstractClass.files;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import jxl.Workbook;
import jxl.write.WritableWorkbook;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

/**
 *
 * @author yangzhen
 */
public class xml extends files {

    @Override
    public void close(Object[] param) {
        if (param != null) {
            for (Object obj : param) {
                try {

                    if (obj instanceof Workbook) {
                        ((Workbook) obj).close();
                        obj = null;
                    }
                    if (obj instanceof WritableWorkbook) {
                        ((WritableWorkbook) obj).write();
                        ((WritableWorkbook) obj).close();
                        obj = null;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean create_folder(String folder_url) {
        boolean bs = false;
        try {
            File directory = new File("");// 设定为当前文件夹
            String[] mes_h = folder_url.toString().split("/");
            String _url = directory.getCanonicalPath().toString();
            for (int i = 0, len = mes_h.length; i < len; i++) {//循环创建文件夹
                String mes_h_l = mes_h[i].toString();
                if (mes_h_l.length() > 0) {
                    _url = _url + "/" + mes_h_l;
                    _url = _url.replace("\\", "/");
                    //System.out.println(_url);
                    File _folder = new File(_url);
                    if (!_folder.exists()) {// 不存在   
                        if (!_folder.isFile()) {//是否是有效文件
                            if (!_folder.isDirectory()) {//是否是有效目录，为空也为无效
                                _folder.mkdir();//创建文件夹
                                //System.out.println("==========>文件夹[" + _url + "]已创建.");
                                bs = true;
                            }
                        }
                    } else {
                        bs = true;
                        //System.out.println("已存在");
                    }
                }
            }
        } catch (Exception e) {
            bs = false;
            System.out.println("创建文件夹异常：");
            System.out.println("==========>：" + e.getMessage().toString());
        }
        return bs;
    }

    @Override
    public boolean delete_folder(String folder_url) {
        boolean bs = false;
        try {

            File directory = new File("");// 设定为当前文件夹
            String _url = directory.getCanonicalPath().toString();
            _url = _url + folder_url;

            File file = new File(_url);

            if (file.exists()) {                    //判断文件是否存在
                if (file.isFile()) {                    //判断是否是文件
                    file.delete();                       //delete()方法 你应该知道 是删除的意思;
                } else if (file.isDirectory()) {              //否则如果它是一个目录
                    File files[] = file.listFiles();               //声明目录下所有的文件 files[];
                    for (int i = 0; i < files.length; i++) {            //遍历目录下所有的文件
                        this.delete_folder(folder_url + "/" + files[i].toString());             //把每个文件 用这个方法进行迭代
                    }
                }
                file.delete();
                bs = true;
            } else {
                System.out.println("所删除的文件夹不存在！" + '\n');
            }

        } catch (Exception e) {
            bs = false;
            System.out.println("删除文件夹异常：");
            System.out.println("==========>：" + e.getMessage().toString());
        }
        return bs;
    }

    @Override
    public boolean delete_file(String file_url) {
        boolean bs = false;
        try {

            File directory = new File("");// 设定为当前文件夹
            String _url = directory.getCanonicalPath().toString();
            _url = _url + file_url;

            File file = new File(_url);

            if (file.exists()) {                    //判断文件是否存在
                if (file.isFile()) {                    //判断是否是文件
                    file.delete();                       //delete()方法 你应该知道 是删除的意思;
                } else if (file.isDirectory()) {              //否则如果它是一个目录
                    File files[] = file.listFiles();               //声明目录下所有的文件 files[];
                    for (int i = 0; i < files.length; i++) {            //遍历目录下所有的文件
                        this.delete_folder(file_url + "/" + files[i].toString());             //把每个文件 用这个方法进行迭代
                    }
                }
                file.delete();
                bs = true;
            } else {
                System.out.println("所删除的文件不存在！" + '\n');
            }
            bs = true;
        } catch (Exception e) {
            bs = false;
            System.out.println("删除文件夹异常：");
            System.out.println("==========>：" + e.getMessage().toString());
        }
        return bs;
    }

    @Override
    public boolean create_file(String file_url) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String read_file(String file_url, String str_content) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void write_file(String file_url, String str_content) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean copy_file(String oldpath, String newpath) {
        boolean _bs = false;
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldpath);
            if (oldfile.exists()) { //文件存在时  
                InputStream inStream = new FileInputStream(oldpath); //读入原文件   
                FileOutputStream fs = new FileOutputStream(newpath);
                byte[] buffer = new byte[2048];
                int length;
                while ((length = inStream.read(buffer)) != -1) {
                    fs.write(buffer, 0, length);
                } //字节数 文件大小 

                inStream.close();
                fs.flush();
                fs.close();
                _bs = true;
            }
        } catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();
        }
        return _bs;
    }

    public Element get_Elements(String file_url) {
        Element roots = null;
        try {
            //指定使用什么解析器
            SAXBuilder builder = new SAXBuilder();
            File directory = new File("");//
            String default_url = directory.getCanonicalPath().toString();
            //得到Document供操作
            Document doc = builder.build(default_url + file_url);
            //System.out.println(default_url + file_url);
            roots = doc.getRootElement();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return roots;
    }

    public List get_ChildrenElements(Element _elements, String _elements_name) {
        List _list = new ArrayList();
        //得到元素（节点）的集合：
        if (_elements_name == null || _elements_name.trim().length() == 0) {
            _list = _elements.getChildren();
        } else {
            _list = _elements.getChildren(_elements_name);
        }
        return _list;
    }

    public String get_Element_Name(Element _element) {
        String str_value = "";
        try {
            str_value = _element.getName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str_value;
    }

    public String get_Element_AttributeValue(Element _element, String _attribute_name) {
        String str_value = "";
        try {
            str_value = _element.getAttributeValue(_attribute_name);//得到节点具体属性值 
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str_value;
    }

    public String get_Element_ChildText(Element _element, String _childtext_name) {
        String str_value = "";
        try {
            str_value = _element.getChildText(_childtext_name);//得到节点具体属性值 
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str_value;
    }

}
