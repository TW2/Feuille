package feuille.module.audio;

import feuille.util.Database;
import feuille.util.Loader;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class WaveDB {

    private final Path dbPath;
    private final String tableName;

    public WaveDB(Path dbPath, String tableName) {
        this.dbPath = dbPath;
        this.tableName = tableName;
    }

    public boolean create() {
        try(Connection c = Database.connect(dbPath)){
            c.createStatement().executeUpdate(String.format(
                    "create table %s (ms integer, image blob)",
                    tableName
            ));
        } catch (SQLException e) {
            Loader.consoleErr(e.getMessage());
            return false;
        }
        return true;
    }

    public boolean add(long ms, BufferedImage image) {
        try(Connection c = Database.connect(dbPath)){
            byte[] bytes = null;

            try(ByteArrayOutputStream out = new ByteArrayOutputStream()){
                ImageIO.write(image, "png", out);
                bytes = out.toByteArray();
            }

            String sql = String.format(
                    "insert into %s (ms, image) values (?, ?)",
                    tableName
            );

            PreparedStatement preparedStatement = c.prepareStatement(sql);

            preparedStatement.setLong(1, ms);
            preparedStatement.setBytes(2, bytes);

            preparedStatement.executeUpdate();

        } catch (SQLException | IOException e) {
            Loader.consoleErr(e.getMessage());
            return false;
        }
        return true;
    }

    public BufferedImage get(long ms_time){
        try(Connection c = Database.connect(dbPath)){

            ResultSet rs = c.createStatement().executeQuery(String.format(
                    "select * from %s where %s = %d",
                    tableName, "ms", ms_time
            ));

            while(rs.next()) {
                byte[] bytes = rs.getBytes("image");
                try(ByteArrayInputStream in = new ByteArrayInputStream(bytes)){
                    BufferedImage img = ImageIO.read(in);
                    if(img != null){
                        return img;
                    }
                }
            }

            return null;
        } catch (SQLException | IOException e) {
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

}
