package interaction;

import java.io.PrintWriter;
import java.util.Scanner;

import app.ServerResponser;
import data.EResponse;


public interface IResponse {

	EResponse response(String[] params, ServerResponser responser, Scanner reader, PrintWriter writer);
}
