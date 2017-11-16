package com.cotans.driverapp;


import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.cotans.driverapp.models.GlobalConstants;
import com.cotans.driverapp.models.network.http.ServerApi;
import com.cotans.driverapp.models.network.http.parcelServer.SignInResponse;
import com.cotans.driverapp.models.network.http.routeServer.RouteServer;
import com.cotans.driverapp.models.scopes.ContextModule;
import com.cotans.driverapp.models.scopes.DaggerMyApplicationComponent;
import com.cotans.driverapp.models.scopes.MyApplicationComponent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.reactivex.Single;
import io.reactivex.observers.DisposableSingleObserver;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static org.junit.Assert.assertEquals;


@RunWith(AndroidJUnit4.class)
public class NetworkTest {

    MyApplicationComponent component = DaggerMyApplicationComponent
            .builder()
            .contextModule(new ContextModule(InstrumentationRegistry.getContext().getApplicationContext()))
            .build();


    @Test
    public void testRouteReceive() {
        String res = "{\"id\":\"59f872a5f232985bddad3467\",\"route\":{\"summary\":\"а/д Волга/E22/М7\",\"legs\":[{\"steps\":[{\"html_instructions\":\"Head \\u003cb\\u003enorthwest\\u003c/b\\u003e on \\u003cb\\u003eул. Университетская\\u003c/b\\u003e\",\"distance\":{\"text\":\"0.1 km\",\"value\":139},\"start_location\":{\"lat\":55.75154999999999,\"lng\":48.747179},\"end_location\":{\"lat\":55.7520555,\"lng\":48.7451507},\"polyline\":{\"points\":\"e~gsI{|ohHeBtK\"},\"steps\":[],\"transit_details\":null,\"travel_mode\":\"DRIVING\",\"duration\":{\"value\":11,\"text\":\"11s\"}},{\"html_instructions\":\"At the roundabout, take the \\u003cb\\u003e3rd\\u003c/b\\u003e exit onto \\u003cb\\u003eул. Спортивная\\u003c/b\\u003e\",\"distance\":{\"text\":\"1.5 km\",\"value\":1503},\"start_location\":{\"lat\":55.7520555,\"lng\":48.7451507},\"end_location\":{\"lat\":55.74271270000001,\"lng\":48.7306255},\"polyline\":{\"points\":\"kahsIepohHIAI?IBIDIFGJCDAFELEVCX?XBV@NDNDLFJFHBDFBDBD@D?B?B?D?DADCDCDCDEBGBGDG@GjB~AjAdAjAfANJ~AxA~A`BtBjCt@~@bBnBjAzA^d@z@nAv@jBx@~AlAlBxApC`ApBjAjCdAhCz@nBb@bAnA~CtAbEtApDjAxD\"},\"steps\":[],\"transit_details\":null,\"travel_mode\":\"DRIVING\",\"duration\":{\"value\":135,\"text\":\"2m15s\"}},{\"html_instructions\":\"Turn \\u003cb\\u003eleft\\u003c/b\\u003e onto \\u003cb\\u003eул. Новоцентральная\\u003c/b\\u003e\",\"distance\":{\"text\":\"3.1 km\",\"value\":3054},\"start_location\":{\"lat\":55.74271270000001,\"lng\":48.7306255},\"end_location\":{\"lat\":55.72729589999999,\"lng\":48.7597551},\"polyline\":{\"points\":\"}ffsImulhHX]vPoStEqFhEcFNQ`AcArFwFfHeHfH_H`K{JrCmCbR{QbK_Kj@m@T]LYFQDU?S?W?QG]WaB}CsN}EcUeCuLE[CYAU@SBUBQFWHUHOl@cA\"},\"steps\":[],\"transit_details\":null,\"travel_mode\":\"DRIVING\",\"duration\":{\"value\":195,\"text\":\"3m15s\"}},{\"html_instructions\":\"Turn \\u003cb\\u003eleft\\u003c/b\\u003e onto \\u003cb\\u003eа/д Волга\\u003c/b\\u003e/\\u003cb\\u003eE22\\u003c/b\\u003e/\\u003cb\\u003eМ7\\u003c/b\\u003e\",\"distance\":{\"text\":\"7.6 km\",\"value\":7555},\"start_location\":{\"lat\":55.72729589999999,\"lng\":48.7597551},\"end_location\":{\"lat\":55.7764967,\"lng\":48.8399309},\"polyline\":{\"points\":\"sfcsIokrhHSy@]sAYeAa@gBy@oDa@iBKa@eAsEk@eCmByIuBaJe@sBACeAaEa@{A_@qAEM{AaFSo@uCgJ}CsIqAmDEM}@yBEMeC{Fi@oAeB_ECGqAkC{@gB_@u@We@y@}Ae@y@Yi@e@w@Wg@KO}@cBQY[g@QYe@y@w@iAkAiBm@}@e@m@m@{@k@y@q@_Ae@w@EMAEEEKMQWOWQWOW[o@_AuA}GsJeFsHsByCeCkDAAeCqDSYcGqI}EcHqGgJeBcC}AyBa@m@wCsEuAwBMQ{BoD]k@_CyDAAaAaBg@y@gBuCqEkHwDmG}EaIyAcCm@cAg@y@Yg@oAuBsC{EuBwDoBwDi@gAcAuB_BeDaAwBs@_BgD}IkF{NoHeWaCgIkCeJQm@Uu@_@eA_@eAk@{AkAyC_AsBWi@Se@S_@g@aAu@mAw@qAc@s@Wa@m@_AGIuBuCYc@GIMQw@cAW_@uAuAcBqA_@Y[UMGIIYQ_@Wo@[_Bu@A?uAi@WKOIOGGAIEWIKCKEQGSGe@QGAaBa@w@Su@MOCIAKA\"},\"steps\":[],\"transit_details\":null,\"travel_mode\":\"DRIVING\",\"duration\":{\"value\":366,\"text\":\"6m6s\"}},{\"html_instructions\":\"Keep \\u003cb\\u003eleft\\u003c/b\\u003e to stay on \\u003cb\\u003eа/д Волга\\u003c/b\\u003e/\\u003cb\\u003eE22\\u003c/b\\u003e/\\u003cb\\u003eМ7\\u003c/b\\u003e\",\"distance\":{\"text\":\"8.7 km\",\"value\":8738},\"start_location\":{\"lat\":55.7764967,\"lng\":48.8399309},\"end_location\":{\"lat\":55.8543612,\"lng\":48.8542468},\"polyline\":{\"points\":\"czlsIq`biH[CYEsBQsAKQASAa@CSCYCWCu@Ec@CUC[A]CAA]?]Aa@?A?]?]?S?m@?o@?m@?u@?{AAaACqBCs@CyCGiBE_BEcBCgBEcCGqAEoEI{DGEAaEKqCEiJ]i@AeIO_Tc@uAEeCGuCI_CG_FKo@?qBGoUg@uCK}AAuFOyFIcFOgFIuAC}GU}E]iEk@qDk@aIgBoD}@yEaBc@OUMsH_Dy@_@mCqAwDkBeBy@QKaEuBAACAuAy@qAq@u@]o@[MGeAa@e@Qw@[QI[Mi@Sy@[wEmAMC{@OsBa@{F{@_LwAc@GmI{@sC]CAoC]cEg@eAO_D[uFs@gFk@ih@iGmH}@mEg@qFq@_AK_CYOAWC{AOcD[MCOC\"},\"steps\":[],\"transit_details\":null,\"travel_mode\":\"DRIVING\",\"duration\":{\"value\":387,\"text\":\"6m27s\"}},{\"html_instructions\":\"Take the exit toward \\u003cb\\u003eКАЗАНЬ\\u003c/b\\u003e/\\u003cb\\u003eKAZAN\\u003c/b\\u003e\",\"distance\":{\"text\":\"0.6 km\",\"value\":594},\"start_location\":{\"lat\":55.8543612,\"lng\":48.8542468},\"end_location\":{\"lat\":55.8569751,\"lng\":48.86132079999999},\"polyline\":{\"points\":\"w`|sIazdiHEUAEEEEEEEIEy@_@m@_@{@i@UQw@cAk@y@o@_A]q@Qc@Oc@Wy@Ou@Kk@Mw@K{@KqAIaAEaAAUA]?aA?i@Be@B_AJcB@K?GAy@\"},\"steps\":[],\"transit_details\":null,\"travel_mode\":\"DRIVING\",\"duration\":{\"value\":32,\"text\":\"32s\"}},{\"html_instructions\":\"Continue onto \\u003cb\\u003eул. Залесная\\u003c/b\\u003e\",\"distance\":{\"text\":\"4.3 km\",\"value\":4264},\"start_location\":{\"lat\":55.8569751,\"lng\":48.86132079999999},\"end_location\":{\"lat\":55.84561799999999,\"lng\":48.9263929},\"polyline\":{\"points\":\"cq|sIgffiHPoANaApAoJnI_n@\\\\gCZwBXwB\\\\gCzFac@lHkk@`DsVd@mDVwB|@eIXqCV}CTcD\\\\eFj@cIdEsn@dA_P`AcObAcOn@}JR_Dj@wIRkCdBgWj@oIf@qG\"},\"steps\":[],\"transit_details\":null,\"travel_mode\":\"DRIVING\",\"duration\":{\"value\":323,\"text\":\"5m23s\"}},{\"html_instructions\":\"Continue onto \\u003cb\\u003eш. Горьковское\\u003c/b\\u003e\",\"distance\":{\"text\":\"7.7 km\",\"value\":7653},\"start_location\":{\"lat\":55.84561799999999,\"lng\":48.9263929},\"end_location\":{\"lat\":55.82475179999999,\"lng\":49.042078},\"polyline\":{\"points\":\"cjzsI}|riHXaCvAqMpGqi@jBeOr@gGbF_b@xCkVx@wGpB}OrGwi@x@wGNiAlCqTVqBzCkW|Fuf@xAcLZoBBOrBqHJa@vEoLFQFO`D_Iz@gC~@qC^yATiATqAVaBVeCXuD~D{s@vLavBf@eJV}ENuAPiCBa@?QAg@DaAJwCDm@Fy@Bc@@URaDFaAD}@D{@d@sIz@wOb@mHL{BVoEJoBFaAJiB\\\\aGd@wHPsCRgDJyALsBBe@TgDJ_BD]Fi@BKD_@Fc@Jo@\"},\"steps\":[],\"transit_details\":null,\"travel_mode\":\"DRIVING\",\"duration\":{\"value\":453,\"text\":\"7m33s\"}},{\"html_instructions\":\"At the roundabout, take the \\u003cb\\u003e1st\\u003c/b\\u003e exit onto \\u003cb\\u003eул. Фрунзе\\u003c/b\\u003e\",\"distance\":{\"text\":\"1.2 km\",\"value\":1239},\"start_location\":{\"lat\":55.82475179999999,\"lng\":49.042078},\"end_location\":{\"lat\":55.81825869999999,\"lng\":49.058015},\"polyline\":{\"points\":\"ugvsI_pijHBGBIBIBGBIBIBIBI@CpAoDv@}BjCyHj@aBr@wBd@uA|CiId@mAHUN_@f@eAFOTm@pCqHhC_Ht@yBDMDSR_A`@qBRmALaAh@wCNy@N_AL_AJq@TeC\"},\"steps\":[],\"transit_details\":null,\"travel_mode\":\"DRIVING\",\"duration\":{\"value\":160,\"text\":\"2m40s\"}},{\"html_instructions\":\"Continue onto \\u003cb\\u003eКраснококшайск урамы\\u003c/b\\u003e/\\u003cb\\u003eул. Краснококшайская\\u003c/b\\u003e\",\"distance\":{\"text\":\"1.2 km\",\"value\":1250},\"start_location\":{\"lat\":55.81825869999999,\"lng\":49.058015},\"end_location\":{\"lat\":55.81286919999999,\"lng\":49.0754551},\"polyline\":{\"points\":\"c_usIssljHTuBR}Bp@sFTyBP{ARuAJ{@\\\\qBbA_FJe@nAcFxD_OvEsQ|FoTVeAJa@@EJk@Fa@Hs@\"},\"steps\":[],\"transit_details\":null,\"travel_mode\":\"DRIVING\",\"duration\":{\"value\":153,\"text\":\"2m33s\"}},{\"html_instructions\":\"Turn \\u003cb\\u003eleft\\u003c/b\\u003e onto \\u003cb\\u003eКраснококшайск урамы\\u003c/b\\u003e/\\u003cb\\u003eул. Большая Крыловка\\u003c/b\\u003e/\\u003cb\\u003eул. Краснококшайская\\u003c/b\\u003e\",\"distance\":{\"text\":\"0.3 km\",\"value\":254},\"start_location\":{\"lat\":55.81286919999999,\"lng\":49.0754551},\"end_location\":{\"lat\":55.8148577,\"lng\":49.076274},\"polyline\":{\"points\":\"m}ssIs`pjHBg@Bk@c@?o@Iw@M]GyBWaAKo@I\"},\"steps\":[],\"transit_details\":null,\"travel_mode\":\"DRIVING\",\"duration\":{\"value\":44,\"text\":\"44s\"}},{\"html_instructions\":\"Keep \\u003cb\\u003eleft\\u003c/b\\u003e to continue on \\u003cb\\u003eул. Большая Крыловка\\u003c/b\\u003e\",\"distance\":{\"text\":\"0.6 km\",\"value\":633},\"start_location\":{\"lat\":55.8148577,\"lng\":49.076274},\"end_location\":{\"lat\":55.8205073,\"lng\":49.0775426},\"polyline\":{\"points\":\"{itsIuepjHy^eFoBW\"},\"steps\":[],\"transit_details\":null,\"travel_mode\":\"DRIVING\",\"duration\":{\"value\":38,\"text\":\"38s\"}},{\"html_instructions\":\"Continue onto \\u003cb\\u003eул. Ленская Лена Ур.\\u003c/b\\u003e\",\"distance\":{\"text\":\"0.4 km\",\"value\":396},\"start_location\":{\"lat\":55.8205073,\"lng\":49.0775426},\"end_location\":{\"lat\":55.82298850000001,\"lng\":49.08170459999999},\"polyline\":{\"points\":\"emusIsmpjHg@Ko@O_@K_@WECOSIKIKGKEKISK_@u@aCY}@K]g@{AyBmHK[\"},\"steps\":[],\"transit_details\":null,\"travel_mode\":\"DRIVING\",\"duration\":{\"value\":56,\"text\":\"56s\"}},{\"html_instructions\":\"Continue straight to stay on \\u003cb\\u003eул. Ленская Лена Ур.\\u003c/b\\u003e\",\"distance\":{\"text\":\"0.3 km\",\"value\":316},\"start_location\":{\"lat\":55.82298850000001,\"lng\":49.08170459999999},\"end_location\":{\"lat\":55.8246401,\"lng\":49.08582759999999},\"polyline\":{\"points\":\"u|usIsgqjH[iAcBuFUs@sDeM\"},\"steps\":[],\"transit_details\":null,\"travel_mode\":\"DRIVING\",\"duration\":{\"value\":20,\"text\":\"20s\"}},{\"html_instructions\":\"Continue onto \\u003cb\\u003eпр. Хусаина Ямашева\\u003c/b\\u003e\",\"distance\":{\"text\":\"0.4 km\",\"value\":375},\"start_location\":{\"lat\":55.8246401,\"lng\":49.08582759999999},\"end_location\":{\"lat\":55.82642079999999,\"lng\":49.0909267},\"polyline\":{\"points\":\"_gvsImarjHMe@c@{AMg@mB}H_CqJu@aD\"},\"steps\":[],\"transit_details\":null,\"travel_mode\":\"DRIVING\",\"duration\":{\"value\":23,\"text\":\"23s\"}},{\"html_instructions\":\"Take the \\u003cb\\u003eИбрагимова просп.\\u003c/b\\u003e exit\",\"distance\":{\"text\":\"0.2 km\",\"value\":163},\"start_location\":{\"lat\":55.82642079999999,\"lng\":49.0909267},\"end_location\":{\"lat\":55.8266921,\"lng\":49.0934737},\"polyline\":{\"points\":\"crvsIiasjHBa@Eu@WmCKyAKqBCiA\"},\"steps\":[],\"transit_details\":null,\"travel_mode\":\"DRIVING\",\"duration\":{\"value\":19,\"text\":\"19s\"}},{\"html_instructions\":\"Turn \\u003cb\\u003eright\\u003c/b\\u003e onto \\u003cb\\u003eпр. Ибрагимова\\u003c/b\\u003e\",\"distance\":{\"text\":\"0.2 km\",\"value\":232},\"start_location\":{\"lat\":55.8266921,\"lng\":49.0934737},\"end_location\":{\"lat\":55.8246629,\"lng\":49.09435089999999},\"polyline\":{\"points\":\"ysvsIeqsjHpHcCbBk@\"},\"steps\":[],\"transit_details\":null,\"travel_mode\":\"DRIVING\",\"duration\":{\"value\":23,\"text\":\"23s\"}},{\"html_instructions\":\"Sharp \\u003cb\\u003eleft\\u003c/b\\u003e to stay on \\u003cb\\u003eпр. Ибрагимова\\u003c/b\\u003e\",\"distance\":{\"text\":\"0.3 km\",\"value\":328},\"start_location\":{\"lat\":55.8246629,\"lng\":49.09435089999999},\"end_location\":{\"lat\":55.8273105,\"lng\":49.0936798},\"polyline\":{\"points\":\"cgvsIuvsjHMaBkHdCgBn@GBIDa@LOFk@P\"},\"steps\":[],\"transit_details\":null,\"travel_mode\":\"DRIVING\",\"duration\":{\"value\":45,\"text\":\"45s\"}},{\"html_instructions\":\"Sharp \\u003cb\\u003eleft\\u003c/b\\u003e to stay on \\u003cb\\u003eпр. Ибрагимова\\u003c/b\\u003e\",\"distance\":{\"text\":\"52 m\",\"value\":52},\"start_location\":{\"lat\":55.8273105,\"lng\":49.0936798},\"end_location\":{\"lat\":55.8270779,\"lng\":49.0932979},\"polyline\":{\"points\":\"uwvsIorsjHH~Ab@S\"},\"steps\":[],\"transit_details\":null,\"travel_mode\":\"DRIVING\",\"duration\":{\"value\":20,\"text\":\"20s\"}},{\"html_instructions\":\"Turn \\u003cb\\u003eright\\u003c/b\\u003e toward \\u003cb\\u003eпр. Хусаина Ямашева\\u003c/b\\u003e\",\"distance\":{\"text\":\"0.1 km\",\"value\":139},\"start_location\":{\"lat\":55.8270779,\"lng\":49.0932979},\"end_location\":{\"lat\":55.8267564,\"lng\":49.0911651},\"polyline\":{\"points\":\"gvvsIcpsjHLjCPtC^fC\"},\"steps\":[],\"transit_details\":null,\"travel_mode\":\"DRIVING\",\"duration\":{\"value\":24,\"text\":\"24s\"}},{\"html_instructions\":\"Take the ramp on the \\u003cb\\u003eleft\\u003c/b\\u003e onto \\u003cb\\u003eпр. Хусаина Ямашева\\u003c/b\\u003e\",\"distance\":{\"text\":\"40 m\",\"value\":40},\"start_location\":{\"lat\":55.8267564,\"lng\":49.0911651},\"end_location\":{\"lat\":55.82653639999999,\"lng\":49.0906715},\"polyline\":{\"points\":\"gtvsIybsjHb@~@Fb@\"},\"steps\":[],\"transit_details\":null,\"travel_mode\":\"DRIVING\",\"duration\":{\"value\":8,\"text\":\"8s\"}},{\"html_instructions\":\"Slight \\u003cb\\u003eright\\u003c/b\\u003e onto \\u003cb\\u003eул. Ямашева\\u003c/b\\u003e\",\"distance\":{\"text\":\"0.2 km\",\"value\":191},\"start_location\":{\"lat\":55.82653639999999,\"lng\":49.0906715},\"end_location\":{\"lat\":55.8257217,\"lng\":49.08803169999999},\"polyline\":{\"points\":\"{rvsIu_sjH?^?R@RHj@BP\\\\nA`@~AXbAx@tC\"},\"steps\":[],\"transit_details\":null,\"travel_mode\":\"DRIVING\",\"duration\":{\"value\":17,\"text\":\"17s\"}}],\"distance\":{\"text\":\"39.1 km\",\"value\":39108},\"start_location\":{\"lat\":55.75154999999999,\"lng\":48.747179},\"end_location\":{\"lat\":55.8257217,\"lng\":49.08803169999999},\"start_address\":\"Universitetskaya St, 7, Innopolis, Respublika Tatarstan, Russia, 420500\",\"end_address\":\"Ulitsa Yamasheva, 4, Kazan, Respublika Tatarstan, Russia, 420080\",\"duration\":{\"value\":2552,\"text\":\"42m32s\"},\"duration_in_traffic\":{\"value\":0,\"text\":\"\"},\"arrival_time\":null,\"departure_time\":null}],\"waypoint_order\":[],\"overview_polyline\":{\"points\":\"e~gsI{|ohHeBtKIASBSLKPGTIp@Bp@F^LXJNXHRAVQN_@rGxF~AxA~A`BjDjEnDjEzAtBpBjElAlBxApClC|F`CxFrBbFtAbEtApDjAxDX]lWa[xEuFtH{HnQeQtOiOf^{]j@m@T]Tk@Di@?i@_@_C{Jwd@kCqMEo@Di@Ji@Re@l@cASy@w@yCiCcLuIu_@oCuKuCqJuCgJ}CsIwA{DsFsMiBgEmCsFwCsFcByC}B_EwEkHqDcFs@qA_AuAk@gA}IiMyImMgCmD{RaYwNsSwJmO{Vsa@mKcQiB}CiGsKyC_GcD{GuBwEsKyYoQan@u@{BkAaDkAyC_AsBk@oA{@aBmB_DqB_DeDuEoAcBuAuAcBqA{@o@WQy@i@oCqA_C_AwBs@m@SyCu@{AU}Fg@{AK}E]_BA}B?oFAaKSuKUaR_@gEMqCEiJ]oJQuVi@{Re@aDGeZs@sIQyFIcFO}HM}GU}E]iEk@qDk@qNeD}FqBiImDeNwGyEeCgDkBeBy@qDwAqCgAeFqAoDq@{F{@_LwAqJcAkNeBeFk@}M_Bw_AaLgFk@}Gs@G[KKwBkAqA{@cB}Bo@_A]q@a@gAg@oBYcBWmCOcCCs@?kBTuEAaA`@qCzMoaAv@_GzFac@lHkk@fEa\\\\tA}Lp@oHr@iKpFwx@fCc`@rDyj@xBs[rAaRpBsQ|Jwy@jNkjAnMweAdDcXxKa_AxAcLZoBvBaIJa@vEoLNa@`D_Iz@gC~@qC^yAj@{CVaBVeCXuD~D{s@~MgaCV}ENuATkDAy@PyERaDzDsr@`Daj@r@{KP}BJu@LcANw@Ne@La@hCmHvD{KxAmEbEwKXu@n@uAfD_JhC_Ht@yBJa@t@qD`@oCx@qE\\\\_C`@wDh@sFfAmJd@qDh@mDnAeGhGcVtMcg@b@gB^gCFsAc@?o@IuAU{Dc@i`@oFwCc@oA[e@[k@w@qAaEgEeNaDoKsFoRmFoTu@aDBa@]cEWkECiApHcCbBk@MaBsKtDQHq@Tk@PH~Ab@SLjCPtC^fCb@~@Fb@?r@J~@`@`BtBxH\"},\"bounds\":{\"northeast\":{\"lat\":55.8570788,\"lng\":49.0948438},\"southwest\":{\"lat\":55.7249251,\"lng\":48.7306255}},\"copyrights\":\"Map data ©2017 Google\",\"warnings\":[],\"fare\":null}}";


        MyApplicationComponent component = DaggerMyApplicationComponent
                .builder()
                .contextModule(new ContextModule(InstrumentationRegistry.getContext().getApplicationContext()))
                .build();


        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GlobalConstants.ROUTE_SERVER_URI_FOR_RETROFIT)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        RouteServer server = retrofit.create(RouteServer.class);
        ServerApi api = component.serverApi();

        String bodyStr = "{\"parcel_id\":" + 4 + "}";

        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain; charset=utf-8"), bodyStr);

        Log.d("NetworkTest", "Making request to route server with body:" + bodyStr);
        Call<String> call = server.getConfirmedRoute(requestBody);

    }

    @Test
    public void testRouteCalculation() {
        ServerApi api = component.serverApi();

        api.getRouteSingle("addr1", "55.752901", "48.741833",
                "addr", "55.751348", "48.708370").subscribe(new DisposableSingleObserver<String>() {
            @Override
            public void onSuccess(String s) {
                try {
                    JSONObject obj = new JSONObject(s);
                    JSONObject route = obj.getJSONObject("route");
                    assertEquals(route.getString("summary"), "ул. Спортивная and ул. Новоцентральная");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.d("onError", e.getMessage());
            }
        });
    }

    @Test
    public void testFirebaseTokenSending() {
        ServerApi api = component.serverApi();
        api.sendMessagingToken("email", "token").subscribe(new DisposableSingleObserver<Response<ResponseBody>>() {
            @Override
            public void onSuccess(Response<ResponseBody> responseBody) {
                assertEquals(200, responseBody.code());
            }

            @Override
            public void onError(Throwable e) {
                assertEquals(true, false);
                dispose();
            }
        });
    }

    @Test
    public void testJsonParser() throws JSONException {

    }

    @Test
    public void testRouteAcceptance() {
        MyApplicationComponent component = DaggerMyApplicationComponent
                .builder()
                .contextModule(new ContextModule(InstrumentationRegistry.getContext()))
                .build();


    }

    @Test
    public void testLoginLogout() {
        MyApplicationComponent component = DaggerMyApplicationComponent
                .builder()
                .contextModule(new ContextModule(InstrumentationRegistry.getContext()))
                .build();

        ServerApi api = component.serverApi();

        Single<SignInResponse> call = api.getSendLoginRequestSingle("dr@mail.com", "driver");

        call.subscribe(new DisposableSingleObserver<SignInResponse>() {
            @Override
            public void onSuccess(SignInResponse signInResponse) {
                assertEquals(signInResponse.getStatus(), "ok");
                String token = signInResponse.getResult().getToken();
                api.getLogoutSingle(token)
                        .subscribe(logOutResponce -> assertEquals(signInResponse.getStatus(), "ok")
                        );
                dispose();
            }

            @Override
            public void onError(Throwable e) {
                assertEquals(false, true);
                dispose();
            }
        });

    }

}
