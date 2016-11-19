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
 * BibTeX.java
 * Copyright (C) 2016 University of Waikato, Hamilton, NZ
 */

package nz.ac.waikato.cms.proceedings.core;

import nz.ac.waikato.cms.core.FileUtils;
import nz.ac.waikato.cms.core.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * BibTeX compiler.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 */
public class BibTeX
  extends AbstractCompiler {

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

    if (!document.toLowerCase().endsWith(".aux"))
      document = FileUtils.replaceExtension(document, ".aux");

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

    if (result != null)
      result = "Failed to execute: " + Utils.flatten(args, " ") + "\n" + result;

    return result;
  }

  /**
   * Runs the compiler from commandline.
   *
   * @param args	the commandline options
   * @throws Exception	if compilation fails
   */
  public static void main(String[] args) throws Exception {
    if (args.length < 2) {
      System.err.println("Usage: BibTeX <executable> [additional options] <document>");
      System.exit(1);
    }

    BibTeX compiler = new BibTeX();
    compiler.setExecutable(args[0]);
    args[0] = "";
    String document = args[args.length - 1];
    compiler.setOptions(args);
    String result = compiler.compile(document);
    if (result != null)
      System.err.println(result);
  }
}
