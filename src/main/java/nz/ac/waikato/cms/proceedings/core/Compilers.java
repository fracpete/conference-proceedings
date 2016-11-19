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
 * Compilers.java
 * Copyright (C) 2016 University of Waikato, Hamilton, NZ
 */

package nz.ac.waikato.cms.proceedings.core;

import org.yaml.snakeyaml.DumperOptions.FlowStyle;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Tag;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Collection of available compilers.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 */
public class Compilers
  extends ArrayList<AbstractCompiler> {

  /**
   * Returns the compiler objects as map objects for yaml.
   *
   * @return		the maps
   */
  public Map[] toYaml() {
    Map[]	result;
    int		i;

    result = new Map[size()];
    for (i = 0; i < size(); i++)
      result[i] = get(i).toYaml();

    return result;
  }

  /**
   * Restores the compilers from the map representations.
   *
   * @param value	the maps
   * @return		the compiler, null if failed to reconstruct
   */
  public static Compilers fromYaml(List value) {
    return fromYaml(value.toArray(new Object[value.size()]));
  }

  /**
   * Restores the compilers from the map representations.
   *
   * @param value	the maps
   * @return		the compiler, null if failed to reconstruct
   */
  public static Compilers fromYaml(Object[] value) {
    Compilers		result;

    result = new Compilers();
    for (Object obj: value)
      result.add(AbstractCompiler.fromYaml((Map<String, Object>) obj));

    return result;
  }

  /**
   * For testing only.
   *
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception {
    Compilers comps = new Compilers();
    LaTeX latex = new LaTeX();
    latex.setExecutable("/usr/bin/pdflatex");
    comps.add(latex);
    BibTeX bibtex = new BibTeX();
    bibtex.setExecutable("/usr/bin/bibtex");
    comps.add(bibtex);
    Yaml yaml = new Yaml();
    String s = yaml.dumpAs(comps.toYaml(), Tag.SEQ, FlowStyle.BLOCK);
    System.out.println(s);
    comps = Compilers.fromYaml((List) yaml.load(s));
    for (AbstractCompiler comp: comps)
      System.out.println(comp);
  }
}
