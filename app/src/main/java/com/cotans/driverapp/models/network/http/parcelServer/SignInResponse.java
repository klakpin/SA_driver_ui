package com.cotans.driverapp.models.network.http.parcelServer;

public class SignInResponse {

    private String status;
    private SignInResponseResult result;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public SignInResponseResult getResult() {
        return result;
    }

    public void setResult(SignInResponseResult result) {
        this.result = result;
    }

    public class SignInResponseResult {

        private Integer id;
        private String token;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}

