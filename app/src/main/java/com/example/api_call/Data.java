package com.example.api_call;

public class Data {
    Result result;

    static class Result {
        Results[] results;

        static class Results {
            String Station;
            String Destination;
        }
    }
}
