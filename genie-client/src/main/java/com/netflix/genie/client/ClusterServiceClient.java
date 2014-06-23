/*
 *
 *  Copyright 2014 Netflix, Inc.
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 *
 */
package com.netflix.genie.client;

import com.google.common.collect.Multimap;
import com.netflix.client.http.HttpRequest;
import com.netflix.client.http.HttpRequest.Verb;
import com.netflix.genie.common.exceptions.CloudServiceException;
import com.netflix.genie.common.model.Cluster;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Singleton class, which acts as the client library for the Cluster
 * Configuration Service.
 *
 * @author skrishnan
 * @author tgianos
 */
//TODO: Can probably templetize the config clients or part of them as
//     much of the code is the same
public final class ClusterServiceClient extends BaseGenieClient {

    private static final Logger LOG = LoggerFactory
            .getLogger(ClusterServiceClient.class);

    private static final String BASE_CONFIG_CLUSTER_REST_URL
            = BASE_REST_URL + "config/clusters";

    // reference to the instance object
    private static ClusterServiceClient instance;

    /**
     * Private constructor for singleton class.
     *
     * @throws IOException if there is any error during initialization
     */
    private ClusterServiceClient() throws IOException {
        super();
    }

    /**
     * Returns the singleton instance that can be used by clients.
     *
     * @return ExecutionServiceClient instance
     * @throws IOException if there is an error instantiating client
     */
    public static synchronized ClusterServiceClient getInstance() throws IOException {
        if (instance == null) {
            instance = new ClusterServiceClient();
        }

        return instance;
    }

    /**
     * Create a new cluster configuration.
     *
     * @param cluster the object encapsulating the new Cluster configuration to
     * create
     *
     * @return extracted cluster configuration response
     * @throws CloudServiceException
     */
    public Cluster createCluster(final Cluster cluster)
            throws CloudServiceException {
        Cluster.validate(cluster);
        final HttpRequest request = this.buildRequest(
                Verb.POST,
                BASE_CONFIG_CLUSTER_REST_URL,
                null,
                cluster);
        return (Cluster) this.executeRequest(request, null, Cluster.class);
    }

    /**
     * Create or update a cluster configuration.
     *
     * @param id the id for the cluster configuration to create or update
     * @param cluster the object encapsulating the new Cluster configuration to
     * create
     *
     * @return extracted cluster configuration response
     * @throws CloudServiceException
     */
    public Cluster updateCluster(
            final String id,
            final Cluster cluster)
            throws CloudServiceException {
        if (StringUtils.isBlank(id)) {
            String msg = "Required parameter id can't be null or empty.";
            LOG.error(msg);
            throw new CloudServiceException(
                    HttpURLConnection.HTTP_BAD_REQUEST, msg);
        }

        final HttpRequest request = this.buildRequest(
                Verb.PUT,
                StringUtils.join(
                        new String[]{BASE_CONFIG_CLUSTER_REST_URL, id},
                        SLASH),
                null,
                cluster);
        return (Cluster) this.executeRequest(request, null, Cluster.class);
    }

    /**
     * Gets information for a given id.
     *
     * @param id the cluster configuration id to get (can't be null or empty)
     * @return the cluster configuration for this id
     * @throws CloudServiceException
     */
    public Cluster getCluster(final String id) throws CloudServiceException {
        if (StringUtils.isBlank(id)) {
            final String msg = "Missing required parameter: id";
            LOG.error(msg);
            throw new CloudServiceException(HttpURLConnection.HTTP_BAD_REQUEST, msg);
        }

        final HttpRequest request = this.buildRequest(
                Verb.GET,
                StringUtils.join(
                        new String[]{BASE_CONFIG_CLUSTER_REST_URL, id},
                        SLASH),
                null,
                null);
        return (Cluster) this.executeRequest(request, null, Cluster.class);
    }

    /**
     * Gets a set of cluster configurations for the given parameters.
     *
     * @param params key/value pairs in a map object.<br>
     *
     * More details on the parameters can be found on the Genie User Guide on
     * GitHub.
     * @return List of cluster configuration elements that match the filter
     * @throws CloudServiceException
     */
    public List<Cluster> getClusterConfigs(final Multimap<String, String> params)
            throws CloudServiceException {
        final HttpRequest request = this.buildRequest(
                Verb.GET,
                BASE_CONFIG_CLUSTER_REST_URL,
                params,
                null);
        return (List<Cluster>) this.executeRequest(request, List.class, Cluster.class);
    }

    /**
     * Delete all the clusters in the database.
     *
     * @return the should be empty set.
     * @throws CloudServiceException
     */
    public List<Cluster> deleteAllClusters() throws CloudServiceException {
        final HttpRequest request = this.buildRequest(
                Verb.DELETE,
                BASE_CONFIG_CLUSTER_REST_URL,
                null,
                null);
        return (List<Cluster>) this.executeRequest(request, List.class, Cluster.class);
    }

    /**
     * Delete a cluster using its id.
     *
     * @param id the id for the cluster cluster to delete
     * @return the deleted cluster cluster
     * @throws CloudServiceException
     */
    public Cluster deleteClusterConfig(final String id) throws CloudServiceException {
        if (StringUtils.isEmpty(id)) {
            String msg = "Missing required parameter: id";
            LOG.error(msg);
            throw new CloudServiceException(HttpURLConnection.HTTP_BAD_REQUEST, msg);
        }

        final HttpRequest request = this.buildRequest(
                Verb.DELETE,
                StringUtils.join(
                        new String[]{BASE_CONFIG_CLUSTER_REST_URL, id},
                        SLASH),
                null,
                null);
        return (Cluster) this.executeRequest(request, null, Cluster.class);
    }
}