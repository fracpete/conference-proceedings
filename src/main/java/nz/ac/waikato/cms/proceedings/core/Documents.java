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
 * Documents.java
 * Copyright (C) 2016 University of Waikato, Hamilton, NZ
 */

package nz.ac.waikato.cms.proceedings.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Collection of documents for proceedings.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 */
public class Documents
  extends ArrayList<Document>
  implements YamlObject<List<Map>> {

  /**
   * Returns the document objects as map objects for yaml.
   *
   * @return		the maps
   */
  public List<Map> toYaml() {
    List<Map>	result;
    int		i;

    result = new ArrayList<>();
    for (i = 0; i < size(); i++)
      result.add(get(i).toYaml());

    return result;
  }

  /**
   * Restores the document from the map representations.
   *
   * @param value	the maps
   * @return		the compiler, null if failed to reconstruct
   */
  public static Documents fromYaml(List value) {
    Documents result;

    result = new Documents();
    for (Object obj: value)
      result.add(Document.fromYaml((Map<String, Object>) obj));

    return result;
  }
}
