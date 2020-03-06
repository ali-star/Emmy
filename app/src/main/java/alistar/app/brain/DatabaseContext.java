package alistar.app.brain;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import java.io.File;
import alistar.app.Utils;

public class DatabaseContext extends ContextWrapper {

    private static final String DEBUG_CONTEXT = "DatabaseContext";

    public DatabaseContext(Context base) {
        super(base);
    }

    @Override
    public File getDatabasePath(String name) {
        String dbfile = Utils.MEMORY_FOLDER + File.separator + Memory.DATABASE_NAME;
        if (!dbfile.endsWith(".db")) {
            dbfile += ".db";
        }

        File result = new File(dbfile);

        if (!result.getParentFile().exists()) {
            result.getParentFile().mkdirs();
        }

        if (Log.isLoggable(DEBUG_CONTEXT, Log.WARN)) {
            Log.w(DEBUG_CONTEXT, "getDatabasePath(" + name + ") = " + result.getAbsolutePath());
        }

        return result;
    }

    /* this version is called for android devices >= api-11. thank to @damccull for fixing this. */
    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory, DatabaseErrorHandler errorHandler) {
        final SQLiteDatabase result = SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), null);
        if (Log.isLoggable(DatabaseContext.DEBUG_CONTEXT, Log.WARN)) {
            Log.w(DatabaseContext.DEBUG_CONTEXT, "openOrCreateDatabase(" + name + ",,) = " + result.getPath());
        }
        return result;
    }
}