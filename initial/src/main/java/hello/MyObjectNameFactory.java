package hello;

import com.codahale.metrics.ObjectNameFactory;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

/**
 * Created by Arvydas on 3/24/15.
 */
public class MyObjectNameFactory implements ObjectNameFactory {
    @Override
    public ObjectName createName(String type, String domain, String name) {
        try {
            return new ObjectName(domain + ":" + type + ":" + name);
        } catch (MalformedObjectNameException e) {
        }
        return null;
    }
}
