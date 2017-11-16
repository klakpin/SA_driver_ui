package com.cotans.driverapp.models.db;


import java.util.List;
import java.util.concurrent.Executor;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class DatabaseApi {

    public AppDatabase db;

    private Executor executor;

    public DatabaseApi(AppDatabase database, Executor executor) {
        this.db = database;
        this.executor = executor;
    }

    public AppDatabase getDb() {
        return db;
    }

    public void setDb(AppDatabase db) {
        this.db = db;
    }

    public void insertOrders(Order... orders) {
        executor.execute(() -> db.OrderDao().insertOrders(orders));
    }

    /**
     * Update order info if order exists, insert if not exists
     *
     * @param orders
     */
    public void insertOnlyNewOrders(List<Order> orders) {
        executor.execute(() -> {
            for (Order order : orders) {
                Order dbOrder = db.OrderDao().getOneOrder(order.getId());
                if (dbOrder == null) {
                    db.OrderDao().insertOrders(order);
                }
            }
        });
    }

//    public Completable updateOrderStatus(int id, String status) {
//        return new Completable() {
//            @Override
//            protected void subscribeActual(CompletableObserver s) {
//                db.OrderDao().getOrderById(id).subscribeOn(Schedulers.computation())
//                        .subscribe(order -> {
//                            if (order != null) {
//                                order.setStatus(status);
//                                db.OrderDao().updateOrders(order);
//                                s.onComplete();
//                            } else {
//                                s.onError(new Throwable("Cannot find order with id " + id + " not found."));
//                            }
//                        });
//            }
//        };
//    }

    public void updateOrderStatus(int id, String status) {
        db.OrderDao().getOrderById(id).subscribeOn(Schedulers.computation())
                .subscribe(order -> {
                    if (order != null) {
                        order.setStatus(status);
                        db.OrderDao().updateOrders(order);

                    }
                });
    }


    public void markOrderAsDelivered(int id) {
        db.OrderDao().getOrderById(id).subscribeOn(Schedulers.computation())
                .subscribe(order -> {
                    if (order != null) {
                        order.setDelivered(true);
                        db.OrderDao().updateOrders(order);

                    }
                });
    }

    public void clearOrdersTable() {
        executor.execute(() -> db.OrderDao().clearTable());
    }


    public void updateOrder(Order currentOrder) {
        executor.execute(() -> db.OrderDao().updateOrders(currentOrder));
    }

    public Maybe<Order> getOrderMaybe(int id) {
        return db.OrderDao()
                .getOrderById(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation());

    }

    public void setRoute(String route, int id) {
        db.OrderDao().getOrderById(id).subscribeOn(Schedulers.computation())
                .subscribe(order -> {
                    if (order != null) {
                        order.setRoute(route);
                        //TODO check it, duplicate stuff
                        db.OrderDao().insertOrders(order);
                        db.OrderDao().updateOrders(order);
                    }
                });
    }

    public Flowable<List<Order>> getAllOrdersFlowable() {
        return db.OrderDao()
                .getAllOrders()
                .subscribeOn(Schedulers.computation());
    }


    public Flowable<List<Order>> getAllUndeliveredOrdersFlowable() {
        return db.OrderDao()
                .getAllUndeliveredOrders()
                .subscribeOn(Schedulers.computation());
    }


    public Flowable<List<Order>> getAllNewOrdersFlowable() {
        return db.OrderDao()
                .getAllNewOrders()
                .subscribeOn(Schedulers.computation());
    }

    public void insertOrders(List<Order> orders) {
        if (orders != null) {
            executor.execute(() -> {
                for (Order ord : orders) {
                    db.OrderDao().insertOrders(ord);
                }
            });
        }
    }

    public void insertNotifications(Notification... notifications) {
        executor.execute(() -> db.notificationDao().insertNotifications(notifications));
    }

    public void updateNotifications(Notification... notifications) {
        executor.execute(() -> db.notificationDao().updateNotifications(notifications));
    }

    public Single<Integer> deleteNotifications(Notification... notifications) {
        return Single.create((SingleOnSubscribe<Integer>) e -> {
            e.onSuccess(db.notificationDao().deleteNotifications(notifications));
        }).subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Flowable<List<Notification>> getAllNotifications() {
        return db.notificationDao().getAllNotifications()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Integer> getNotificationsNumber() {
        return db.notificationDao().getNotificationsNumber()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public void clearNotificationsTable() {
        executor.execute(() -> db.notificationDao().clearTable());
    }

}
