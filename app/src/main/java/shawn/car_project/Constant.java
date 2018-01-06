package shawn.car_project;

/**
 * Created by sshss on 2017/11/13.
 */
import android.util.Log;

public class Constant {

    public static final String PREF_KEY_ROUTER_URL = "pref_key_router_url";
    public static final String PREF_KEY_CAMERA_URL = "pref_key_camera_url";

    public static final String PREF_KEY_TEST_MODE_ENABLED = "pref_key_test_enabled";
    public static final String PREF_KEY_ROUTER_URL_TEST = "pref_key_router_url_test";
    public static final String PREF_KEY_CAMERA_URL_TEST = "pref_key_camera_url_test";

    public static final String PREF_KEY_LEN_ON = "pref_key_len_on";
    public static final String PREF_KEY_LEN_OFF = "pref_key_len_off";

    public static final String DEFAULT_VALUE_CAMERA_URL = "http://192.168.1.1:8080/?action=stream";
    public static final String DEFAULT_VALUE_ROUTER_URL = "192.168.1.1:2001";
    public static final String DEFAULT_VALUE_CAMERA_URL_TEST = "";
    public static final String DEFAULT_VALUE_ROUTER_URL_TEST = "192.168.1.1:2001";
}