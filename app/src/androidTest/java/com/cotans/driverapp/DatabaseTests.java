package com.cotans.driverapp;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.cotans.driverapp.models.GlobalConstants;
import com.cotans.driverapp.models.db.DatabaseApi;
import com.cotans.driverapp.models.db.Order;
import com.cotans.driverapp.models.scopes.ContextModule;
import com.cotans.driverapp.models.scopes.DaggerMyApplicationComponent;
import com.cotans.driverapp.models.scopes.MyApplicationComponent;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class DatabaseTests {

    MyApplicationComponent component = DaggerMyApplicationComponent
            .builder()
            .contextModule(new ContextModule(InstrumentationRegistry.getTargetContext()))
            .build();

    DatabaseApi databaseApi = component.database();

    @Test
    public void CheckWriteRead() {

        Order ord1 = new Order(1, "Sample 1", "Pushkina", "Kolotushkina");
        Order ord2 = new Order(2, "Sample 2", "Italy", "Innopolis");

        ord1.setStatus(GlobalConstants.PARCEL_STATUS_CONFIRMED_BY_DRIVER);
        ord2.setStatus(GlobalConstants.PARCEL_STATUS_PICKED_UP);

        databaseApi.insertOrders(ord1, ord2);

        databaseApi.getOrderMaybe(1)
                .subscribe(order -> {
                    assertEquals(ord1.getName(), order.getName());
                    assertEquals(ord1.getId(), order.getId());
                    assertEquals(ord1.getSource(), order.getSource());
                    assertEquals(ord1.getDestination(), order.getDestination());
                    assertEquals(ord1.getStatus(), order.getStatus());
                });

        databaseApi.getOrderMaybe(2)
                .subscribe(order -> {
                    assertEquals(ord2.getName(), order.getName());
                    assertEquals(ord2.getId(), order.getId());
                    assertEquals(ord2.getSource(), order.getSource());
                    assertEquals(ord2.getDestination(), order.getDestination());
                    assertEquals(ord2.getStatus(), order.getStatus());

                });
    }

    @Test
    public void checkOrderChange() {
        Order ord1 = new Order(1, "Sample 1", "Pushkina", "Kolotushkina");
        ord1.setStatus(GlobalConstants.PARCEL_STATUS_CONFIRMED_BY_DRIVER);

        databaseApi.insertOrders(ord1);

        databaseApi.getOrderMaybe(1)
                .subscribe(order -> {
                    assertEquals(ord1.getName(), order.getName());
                    assertEquals(ord1.getId(), order.getId());
                    assertEquals(ord1.getSource(), order.getSource());
                    assertEquals(ord1.getDestination(), order.getDestination());
                    assertEquals(ord1.getStatus(), order.getStatus());


                    ord1.setStatus(GlobalConstants.PARCEL_STATUS_PICKED_UP);
                    databaseApi.updateOrder(ord1);

                    databaseApi.getOrderMaybe(1)
                            .subscribe(order1 -> {
                                assertEquals(ord1.getName(), order1.getName());
                                assertEquals(ord1.getId(), order1.getId());
                                assertEquals(ord1.getSource(), order1.getSource());
                                assertEquals(ord1.getDestination(), order1.getDestination());
                                assertEquals(ord1.getStatus(), order1.getStatus());
                            });

                });
    }

    @Test
    public void checkOrderCleaning() {
        databaseApi.clearOrdersTable();
    }
}
