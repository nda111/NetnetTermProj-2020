package interaction;

import java.io.PrintWriter;
import java.util.Scanner;

import data.EResponse;


public interface IResponse {

	EResponse response(String[] params, Scanner reader, PrintWriter writer);
}
