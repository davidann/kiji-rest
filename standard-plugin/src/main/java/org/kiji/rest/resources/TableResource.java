/**
 * (c) Copyright 2013 WibiData, Inc.
 *
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kiji.rest.resources;

import static org.kiji.rest.RoutesConstants.INSTANCE_PARAMETER;
import static org.kiji.rest.RoutesConstants.TABLE_PARAMETER;
import static org.kiji.rest.RoutesConstants.TABLE_PATH;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import com.yammer.metrics.annotation.Timed;

import org.kiji.annotations.ApiAudience;
import org.kiji.annotations.ApiStability;
import org.kiji.rest.KijiClient;
import org.kiji.schema.Kiji;
import org.kiji.schema.avro.TableLayoutDesc;

/**
 * This REST resource interacts with Kiji tables.
 *
 * This resource is served for requests using the resource identifier:
 * <li>/v1/instances/&lt;instance&gt;/tables/&lt;table&gt;
 */
@Path(TABLE_PATH)
@Produces(MediaType.APPLICATION_JSON)
@ApiAudience.Public
public class TableResource {
  private final KijiClient mKijiClient;

  /**
   * Default constructor.
   *
   * @param kijiClient that this should use for connecting to Kiji.
   */
  public TableResource(KijiClient kijiClient) {
    mKijiClient = kijiClient;
  }

  /**
   * GETs the layout of the specified table.
   *
   * @param instance in which the table resides.
   * @param table to get the layout for.
   * @return the layout of the specified table
   */
  @GET
  @Timed
  @ApiStability.Evolving
  public TableLayoutDesc getTable(@PathParam(INSTANCE_PARAMETER) String instance,
      @PathParam(TABLE_PARAMETER) String table) {
    final Kiji kiji = mKijiClient.getKiji(instance);
    TableLayoutDesc layout = null;
    try {
      layout = kiji.getMetaTable().getTableLayout(table).getDesc();
    } catch (IOException e) {
      throw new WebApplicationException(e, Status.INTERNAL_SERVER_ERROR);
    }
    return layout;
  }
}
