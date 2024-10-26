import java.util.List;
import java.util.stream.Collectors;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws Exception {
        QueryExpression fieldExp = new FieldExpression("name");
        QueryExpression filterExp1 = new FilterExpression("age", ">", "30");
        QueryExpression filterExp2 = new FilterExpression("status", "=", "active");
        QueryExpression orderExp = new OrderExpression("name", "asc");

        QueryExpression combinedFilter = new AndExpression(Arrays.asList(filterExp1, filterExp2, orderExp, fieldExp));

        ServerContext context = new ServerContext("https://api.example.com/users");
        String result = context.executeQuery(combinedFilter);
        System.out.println(result);
    }
}

class ServerContext {
    private String serverUrl;

    public ServerContext(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public String executeQuery(QueryExpression query) throws Exception {
        String fullUrl = serverUrl + "?" + query.interpret();
        URL url = new URL(fullUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode == 200) {
            return "Query executed successfully: " + fullUrl;
        } else {
            return "Error: " + responseCode;
        }
    }
}


interface QueryExpression {
    String interpret();
}


class FieldExpression implements QueryExpression {
    private String fieldName;

    public FieldExpression(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public String interpret() {
        return "fields=" + fieldName;
    }
}
class FilterExpression implements QueryExpression {
    private String field;
    private String operator;
    private String value;

    public FilterExpression(String field, String operator, String value) {
        this.field = field;
        this.operator = operator;
        this.value = value;
    }

    @Override
    public String interpret() {
        return "filter=" + field + operator + value;
    }
}

class OrderExpression implements QueryExpression {
    private String field;

    private String direction;

    public OrderExpression(String field, String direction) {
        this.field = field;
        this.direction = direction;
    }

    @Override
    public String interpret() {
        return "order=" + field + ":" + direction;
    }
}


class AndExpression implements QueryExpression {
    private List<QueryExpression> expressions;

    public AndExpression(List<QueryExpression> expressions) {
        this.expressions = expressions;
    }

    @Override
    public String interpret() {
        return expressions.stream()
                .map(QueryExpression::interpret)
                .collect(Collectors.joining("&"));
    }
}

