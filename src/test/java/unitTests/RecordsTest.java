package unitTests;

import org.junit.jupiter.api.Test;
import server.database.DbQueueItem;
import server.raw.RawQueueItem;
import server.transformation.TransformQueueItem;

import static org.junit.jupiter.api.Assertions.*;

public class RecordsTest {

    @Test
    void testTransformQueueItem() {
        TransformQueueItem item = new TransformQueueItem("http://example.com", "<html>...</html>");
        assertEquals("http://example.com", item.link());
        assertEquals("<html>...</html>", item.body());
    }

    @Test
    void testRawQueueItem() {
        RawQueueItem raw = new RawQueueItem("http://example.com", 2);
        assertEquals("http://example.com", raw.message());
        assertEquals(2, raw.level());
    }

    @Test
    void testDbQueueItem() {
        DbQueueItem db = new DbQueueItem("http://example.com", "{\"json\":\"value\"}");
        assertEquals("http://example.com", db.link());
        assertEquals("{\"json\":\"value\"}", db.json());
    }
}
