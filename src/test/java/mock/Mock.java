package mock;

public class Mock {
    public static String getFindBySoldStatusBody() {
        return MockUtil.streamToString(MockUtil.getAsStream("mock.json"));
    }
}
