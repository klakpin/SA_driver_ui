package com.cotans.driverapp;


import com.cotans.driverapp.models.InputValidator;
import com.cotans.driverapp.models.network.http.ServerApi;
import com.cotans.driverapp.models.network.http.parcelServer.SignInResponse;
import com.cotans.driverapp.models.session.SessionManager;
import com.cotans.driverapp.presenters.LoginScreenPresenter;
import com.cotans.driverapp.views.Interfaces.LoginActivityInterface;
import com.google.firebase.iid.FirebaseInstanceId;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.lang.reflect.Field;

import io.reactivex.Single;
import io.reactivex.annotations.Nullable;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.BufferedSource;
import retrofit2.Response;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LoginScreenPresenterTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private LoginActivityInterface view;
    @Mock
    private ServerApi serverApi;
    @Mock
    private SessionManager sessionManager;
    @Mock
    private InputValidator validator;

    @Mock
    private FirebaseInstanceId firebaseInstanceId;

    @Test
    public void createMock() {
        new LoginScreenPresenter(serverApi, sessionManager, validator, firebaseInstanceId);
    }

    @Test
    public void viewShouldBeBinded() {
        LoginScreenPresenter presenter = new LoginScreenPresenter(serverApi, sessionManager, validator, firebaseInstanceId);

        presenter.bindView(view);

        Class<?> clazz = presenter.getClass();
        Field field = null;
        try {
            field = clazz.getDeclaredField("view");
        } catch (NoSuchFieldException e) {
            assertThat(false, is(true));
            e.printStackTrace();
        }
        assert field.equals(view);
    }

    @Test
    public void shouldlAskForPermissionsAndBindListeners() {
        LoginScreenPresenter presenter = new LoginScreenPresenter(serverApi, sessionManager, validator, firebaseInstanceId);
        presenter.bindView(view);

        presenter.init();

        verify(view, times(1)).askPermissions();
        verify(view, times(1)).setLoginButtonListener(any());
    }

    @Test
    public void shouldOpenMainActivityOnSuccessfulLogin() {
        LoginScreenPresenter presenter = new LoginScreenPresenter(serverApi, sessionManager, validator, firebaseInstanceId);
        presenter.bindView(view);

        String token = "token";
        String email = "email";
        String firebaseToken = "fbtoken";
        Single<Response<ResponseBody>> single = Single.create(e -> {
            Response<ResponseBody> response = Response.success(new ResponseBody() {
                @Nullable
                @Override
                public MediaType contentType() {
                    return null;
                }

                @Override
                public long contentLength() {
                    return 0;
                }

                @Override
                public BufferedSource source() {
                    return null;
                }
            });
            e.onSuccess(response);
        });

        when(firebaseInstanceId.getToken()).thenReturn(firebaseToken);
        when(view.getEmail()).thenReturn(email);
        when(serverApi.sendMessagingToken(email, firebaseToken)).thenReturn(single);

        presenter.onSuccessfulLogin(token);

        verify(sessionManager, times(1)).setAuthorised();
        verify(sessionManager, times(1)).setEmail(email);
        verify(sessionManager, times(1)).setToken(token);
        verify(view, times(1)).stopProgressBar();
        verify(view, times(1)).openMainActivity();
        verify(view, times(1)).stop();
    }

    @Test
    public void shouldNotifyUserThatEmailIsUnregistered() {
        LoginScreenPresenter presenter = new LoginScreenPresenter(serverApi, sessionManager, validator, firebaseInstanceId);
        presenter.bindView(view);

        presenter.onUnsuccessfulLogin("no_email");

        verify(view, times(1)).stopProgressBar();
        verify(view, times(1)).onUnregistered();
        verify(view, never()).onWrongEmailOrPassword();
        verify(view, never()).onLoginFailure(anyString());
    }

    @Test
    public void shouldNotifyUserThatPasswordWrong() {
        LoginScreenPresenter presenter = new LoginScreenPresenter(serverApi, sessionManager, validator, firebaseInstanceId);
        presenter.bindView(view);

        presenter.onUnsuccessfulLogin("wrong_pswd");

        verify(view, times(1)).stopProgressBar();
        verify(view, never()).onUnregistered();
        verify(view, times(1)).onWrongEmailOrPassword();
        verify(view, never()).onLoginFailure(anyString());
    }

    @Test
    public void shouldNotifyUserAboutUnknownError() {
        LoginScreenPresenter presenter = new LoginScreenPresenter(serverApi, sessionManager, validator, firebaseInstanceId);
        presenter.bindView(view);

        presenter.onUnsuccessfulLogin("int_error");

        verify(view, times(1)).stopProgressBar();
        verify(view, never()).onUnregistered();
        verify(view, never()).onWrongEmailOrPassword();
        verify(view, times(1)).onLoginFailure(anyString());
    }

    @Test
    public void shouldValidateEmail() {
        LoginScreenPresenter presenter = new LoginScreenPresenter(serverApi, sessionManager, validator, firebaseInstanceId);
        presenter.bindView(view);

        when(view.getEmail()).thenReturn("abracadabra");
        when(validator.isEmailValid(anyString())).thenReturn(false);

        presenter.onLoginButtonPressed();

        verify(view, times(1)).onInvalidEmail();
    }

    @Test
    public void shouldValidatePassword() {
        LoginScreenPresenter presenter = new LoginScreenPresenter(serverApi, sessionManager, validator, firebaseInstanceId);
        presenter.bindView(view);

        when(view.getEmail()).thenReturn("email@mail.com");
        when(view.getPassword()).thenReturn("");
        when(validator.isEmailValid(anyString())).thenReturn(true);
        when(validator.isPasswordValid(anyString())).thenReturn(false);

        presenter.onLoginButtonPressed();

        verify(view, never()).onInvalidEmail();
        verify(view, times(1)).onInvalidPassword();
    }

    @Test
    public void shouldLoginWhenLoginAndPasswordAreValid() {
        LoginScreenPresenter presenter = new LoginScreenPresenter(serverApi, sessionManager, validator, firebaseInstanceId);
        presenter.bindView(view);

        String drTok = "token";

        SignInResponse response = mock(SignInResponse.class);
        SignInResponse.SignInResponseResult result = mock(SignInResponse.SignInResponseResult.class);
        Single<SignInResponse> single = Single.create(e -> e.onSuccess(response));
        Single<Response<ResponseBody>> fbsTokSingle = Single.create(e -> {
            Response<ResponseBody> resp = Response.success(new ResponseBody() {
                @Nullable
                @Override
                public MediaType contentType() {
                    return null;
                }

                @Override
                public long contentLength() {
                    return 0;
                }

                @Override
                public BufferedSource source() {
                    return null;
                }
            });
            e.onSuccess(resp);
        });
        when(result.getToken()).thenReturn(drTok);
        when(response.getResult()).thenReturn(result);
        when(serverApi.getSendLoginRequestSingle(any(), any())).thenReturn(single);
        when(serverApi.sendMessagingToken(any(), any())).thenReturn(fbsTokSingle);
        when(view.getEmail()).thenReturn("email@mail.com");
        when(view.getPassword()).thenReturn("password");
        when(validator.isEmailValid(anyString())).thenReturn(true);
        when(validator.isPasswordValid(anyString())).thenReturn(true);

        presenter.onLoginButtonPressed();

        verify(view, never()).onInvalidEmail();
        verify(view, never()).onInvalidPassword();
        verify(sessionManager, times(1)).setAuthorised();
        verify(sessionManager, times(1)).setEmail("email@mail.com");
        verify(sessionManager, times(1)).setToken("token");
        verify(view, times(1)).stopProgressBar();
        verify(view, times(1)).openMainActivity();
        verify(view, times(1)).stop();

    }

}

