public class SessionManager {
    private static int userId = -1;

    public static void setUserId(int id) {
        userId = id;
    }

    public static int getUserId() {
        return userId;
    }

    public static void clearSession() {
        userId = -1;
    }
}
