package feuille.scripting;

import feuille.io.Database;
import feuille.io.Exchange;
import feuille.io.assa.AssEffect;
import feuille.util.Loader;

import java.io.File;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EffectsDB {

    private final Path dbPath;
    private final String tableName;

    public EffectsDB(Exchange exchange){
        if(exchange.getSettings().getCodeMethodsPath() == null){
            File app = new File(new File("").getAbsolutePath());
            dbPath = Path.of(app + "/settings/scripting/sfx/sfx.db");
        }else{
            dbPath = Path.of(exchange.getSettings().getCodeMethodsPath());;
        }
        tableName = "code_methods";
    }

    public boolean create() {
        try(Connection c = Database.connect(dbPath)){
            c.createStatement().executeUpdate(String.format(
                    "create table if not exists %s (col text, name text, type text, data text)",
                    tableName
            ));
        } catch (SQLException e) {
            Loader.consoleErr(e.getMessage());
            return false;
        }
        return true;
    }

    public boolean add(AssEffect codeMethod, String colName) {
        try(Connection c = Database.connect(dbPath)){

            String sql = String.format(
                    "insert into %s (col, name, type, data) values (?, ?, ?, ?)",
                    tableName
            );

            PreparedStatement preparedStatement = c.prepareStatement(sql);

            preparedStatement.setString(1, colName);
            preparedStatement.setString(2, codeMethod.getName());
            preparedStatement.setString(3, codeMethod.getMethodType().getName());
            preparedStatement.setString(4, stringifyData(codeMethod.getEffects()));

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            Loader.consoleErr(e.getMessage());
            return false;
        }
        return true;
    }

    public List<AssEffect> getAssEffects(){
        try(Connection c = Database.connect(dbPath)){

            ResultSet rs = c.createStatement().executeQuery(String.format(
                    "select * from %s",
                    tableName
            ));

            List<AssEffect> codeMethods = new ArrayList<>();

            while(rs.next()) {
                String colName = rs.getString("col");
                String name = rs.getString("name");
                AssEffect.MethodType type = AssEffect.MethodType.of(rs.getString("type"));
                List<String> data = stringToData(rs.getString("data"));

                AssEffect m = new AssEffect();
                m.setCollectionName(colName);
                m.setName(name);
                m.setMethodType(type);
                m.setEffects(data);

                codeMethods.add(m);
            }

            return codeMethods;
        } catch (SQLException e) {
            Loader.consoleErr(e.getMessage());
            return null;
        }
    }

    public boolean clear(){
        try(Connection c = Database.connect(dbPath)){
            c.createStatement().executeUpdate(String.format(
                    "drop table if exists %s",
                    tableName
            ));
        } catch (SQLException e) {
            Loader.consoleErr(e.getMessage());
            return false;
        }
        return true;
    }

    public static String stringifyData(List<String> data){
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<data.size(); i++){
            sb.append(data.get(i));
            if(i != data.size() - 1){
                sb.append(";;;");
            }
        }
        return sb.toString();
    }

    public static List<String> stringToData(String text){
        return new ArrayList<>(Arrays.stream(text.split(";;;")).toList());
    }

}
