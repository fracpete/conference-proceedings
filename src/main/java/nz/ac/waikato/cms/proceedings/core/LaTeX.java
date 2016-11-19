/*
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * LaTeX.java
 * Copyright (C) 2016 University of Waikato, Hamilton, NZ
 */

package nz.ac.waikato.cms.proceedings.core;

import nz.ac.waikato.cms.core.FileUtils;
import nz.ac.waikato.cms.core.Utils;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * LaTeX-based compiler (eg pdflatex, luatex, xetex).
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 */
public class LaTeX
  extends AbstractCompiler {

  /**
   * Checks the log using the specified regular expression.
   *
   * @param document 	the tex document
   * @param regExp	the regular expression to use for matching lines in the log
   * @return		true if at least one line matched the regexp
   */
  protected boolean checkLog(String document, String regExp) {
    String	log;
    File	file;
    String[]	lines;

    log    = FileUtils.replaceExtension(document, ".log");
    file   = new File(log);
    if (!file.exists())
      return false;

    try {
      lines = new String(Files.readAllBytes(file.toPath())).split("\n");
      for (String line: lines) {
	if (line.matches(regExp))
	  return true;
      }
    }
    catch (Exception e) {
      System.err.println("Failed to check log: " + log + "\n" + Utils.throwableToString(e));
    }

    return false;
  }

  /**
   * Compiles the specified document.
   *
   * @param document	the document to compile
   * @return		null if successful, otherwise error message
   */
  @Override
  protected String doCompile(String document) {
    String		result;
    ProcessBuilder	pb;
    List<String>	args;
    Process		proc;
    int			code;

    result = null;

    args = new ArrayList<>();
    args.add(m_Executable);
    if (m_Options != null)
      args.addAll(Arrays.asList(m_Options));
    args.add(document);
    pb = new ProcessBuilder(args);
    pb.directory(new File(document).getParentFile());
    try {
      proc = pb.start();
      code = proc.waitFor();
      if (code != 0)
	result = "Process exited with: " + code;
    }
    catch (Exception e) {
      result = Utils.throwableToString(e);
    }

    if (result == null) {
      if (checkLog(document, ".*Emergency stop.*"))
	result = "Compilation failed!";
    }

    if (result != null)
      result = "Failed to execute: " + Utils.flatten(args, " ") + "\n" + result;

    return result;
  }

  /**
   * Checks the log file associated with the specified tex document whether
   * a recompile is necessary.
   *
   * @param document	the tex document
   * @return		true if requires recompile
   */
  public boolean requiresRecompile(String document) {
    return checkLog(document, ".*(Rerun to get citations correct|Rerun to get cross-references right).*");
  }

  /**
   * Runs the compiler from commandline.
   *
   * @param args	the commandline options
   * @throws Exception	if compilation fails
   */
  public static void main(String[] args) throws Exception {
    if (args.length < 2) {
      System.err.println("Usage: LaTeX <executable> [additional options] <document>");
      System.exit(1);
    }

    LaTeX compiler = new LaTeX();
    compiler.setExecutable(args[0]);
    args[0] = "";
    String document = args[args.length - 1];
    compiler.setOptions(args);
    String result = compiler.compile(document);
    if (result != null)
      System.err.println(result);
    else
      System.out.println("Requires recompile: " + compiler.requiresRecompile(document));
  }
}
