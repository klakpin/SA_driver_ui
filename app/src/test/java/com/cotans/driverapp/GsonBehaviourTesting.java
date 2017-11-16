package com.cotans.driverapp;


import com.cotans.driverapp.models.network.http.parcelServer.GetDriverDeliveriesResponse;
import com.google.gson.Gson;

import org.junit.Test;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;

public class GsonBehaviourTesting {
    @Test
    public void gsonShouldConvertAllFields() {
        String json = "{\n" +
                "  \"status\" : \"ok\",\n" +
                "  \"result\" :\n" +
                "  [\n" +
                "    {\n" +
                "      \"id\": 6,\n" +
                "      \"name\": \"Toys\",\n" +
                "      \"source\": \"Vaskova str, 1-2\",\n" +
                "      \"destination\": \"Foo str, 2-3\",\n" +
                "      \"urgency\": \"low\",\n" +
                "      \"type\": \"special\",\n" +
                "      \"status\": \"created\"\n" +
                "    }\n" +
                "  ]\n" +
                "}\n";


        GetDriverDeliveriesResponse resp = new Gson().fromJson(json, GetDriverDeliveriesResponse.class);
        assertThat(resp.getStatus(), is("ok"));
        assertThat(resp.getResult().get(0).getId() , is(6));
        assertThat (resp.getResult().get(0).getName(), is("Toys"));
        assertThat(resp.getResult().get(0).getSource(), is("Vaskova str, 1-2"));
        assertThat (resp.getResult().get(0).getDestination(), is("Foo str, 2-3"));
        assertThat (resp.getResult().get(0).getUrgency(), is("low"));
        assertThat (resp.getResult().get(0).getType(), is("special"));
        assertThat(resp.getResult().get(0).getStatus(), is("created"));
    }
}
