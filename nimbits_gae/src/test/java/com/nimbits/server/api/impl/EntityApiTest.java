package com.nimbits.server.api.impl;

import com.nimbits.client.enums.Action;
import com.nimbits.client.enums.EntityType;
import com.nimbits.client.enums.ProtectionLevel;
import com.nimbits.client.model.category.Category;
import com.nimbits.client.model.category.CategoryFactory;
import com.nimbits.client.model.category.CategoryModel;
import com.nimbits.client.model.entity.Entity;
import com.nimbits.client.model.entity.EntityModelFactory;
import com.nimbits.client.model.point.Point;
import com.nimbits.client.model.point.PointModelFactory;
import com.nimbits.server.NimbitsServletTest;
import com.nimbits.server.api.EntityApi;
import com.nimbits.server.gson.GsonFactory;
import com.nimbits.server.transaction.entity.EntityServiceFactory;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

/**
 * Empathy Lab
 * User: benjamin
 * Date: 1/7/13
 * Time: 1:24 PM
 */
public class EntityApiTest extends NimbitsServletTest {


    public MockHttpServletRequest req1;
    public MockHttpServletResponse resp1;

    @Before
    public void setup() {
        super.setup();
        req1 = new MockHttpServletRequest();
        resp1 = new MockHttpServletResponse();
    }

    @Test
    public void testPostDeletePoint() throws IOException, ServletException, Exception {
        req.removeAllParameters();


        req.addParameter("id", point.getKey());
        req.addParameter("action", "delete");
        req.addParameter("type", "point");
        req.setMethod("POST");
        entityApi.doPost(req, resp);

        List<Entity> r = EntityServiceFactory.getInstance(engine).getEntityByKey(user, point.getKey(), EntityType.point);
        assertTrue(r.isEmpty());


    }

    @Test
    public void testPostCreatePointConflict() throws IOException, ServletException {
        req1.removeAllParameters();
        String pointJson = GsonFactory.getInstance().toJson(point);


        req1.addParameter("action", "create");
        req1.addParameter("json", pointJson);
        req1.setMethod("POST");
        entityApi.doPost(req1, resp1);

        assertEquals(HttpServletResponse.SC_CONFLICT, resp1.getStatus());
//        List<Entity> r =  EntityServiceImpl.getEntityByKey(user, point.getKey(), EntityType.point);
//        assertTrue(r.isEmpty());


    }

    @Test
    public void testCreateFolderIfMissing() throws IOException, ServletException, Exception {
        req.removeAllParameters();

        Entity e = EntityModelFactory.createEntity("test", "",
                EntityType.category,
                ProtectionLevel.everyone,
                user.getKey(),
                user.getKey());

        Category category = CategoryFactory.createCategory(e);

        String j = GsonFactory.getInstance().toJson(category);

        MockHttpServletRequest req2 = new MockHttpServletRequest();
        MockHttpServletResponse resp2 = new MockHttpServletResponse();

        req.addParameter("action", Action.createmissing.getCode());
        req.addParameter("json", j);
        req.setMethod("POST");
        entityApi.doPost(req, resp);
        String re = resp.getContentAsString();
        Entity ex = GsonFactory.getInstance().fromJson(re, CategoryModel.class);
        assertNotNull(ex);
        assertEquals(ex.getName(), category.getName());
        assertEquals(resp.getHeader(EntityApi.SERVER_RESPONSE), EntityApi.CREATING_ENTITY);


        req.removeAllParameters();
        req2.addParameter("action", Action.createmissing.getCode());
        req2.addParameter("json", j);
        req2.setMethod("POST");
        entityApi.doPost(req2, resp2);
        assertEquals(resp2.getHeader(EntityApi.SERVER_RESPONSE), EntityApi.ENTITY_ALREADY_EXISTS);

//        List<Entity> r =  EntityServiceImpl.getEntityByKey(user, point.getKey(), EntityType.point);
//        assertTrue(r.isEmpty());


    }

    @Test
    public void testPostCreatePoint() throws IOException, ServletException  {
        req.removeAllParameters();
        Point point1 = PointModelFactory.createPointModel(user, UUID.randomUUID().toString());
        String pointJson = GsonFactory.getInstance().toJson(point1);
        req.addParameter("action", "create");
        req.addParameter("json", pointJson);
        req.setMethod("POST");
        entityApi.doPost(req, resp);
        assertEquals(HttpServletResponse.SC_OK, resp.getStatus());


//        List<Entity> r =  EntityServiceImpl.getEntityByKey(user, point.getKey(), EntityType.point);
//        assertTrue(r.isEmpty());


    }

    @Test
    public void testPostUpdatePoint() throws IOException, ServletException, Exception {
        req.removeAllParameters();
        point.setUnit("foo");
        String pointJson = GsonFactory.getInstance().toJson(point);

        req.addParameter("action", "update");
        req.addParameter("json", pointJson);
        req.setMethod("POST");
        entityApi.doPost(req, resp);
        assertEquals(HttpServletResponse.SC_OK, resp.getStatus());
        List<Entity> sample = EntityServiceFactory.getInstance(engine).getEntityByKey(user, point.getKey(), EntityType.point);
        assertFalse(sample.isEmpty());
        Point p = (Point) sample.get(0);
        assertEquals("foo", p.getUnit());


//        List<Entity> r =  EntityServiceImpl.getEntityByKey(user, point.getKey(), EntityType.point);
//        assertTrue(r.isEmpty());


    }


}


