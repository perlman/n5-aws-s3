/**
 * License: GPL
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License 2
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.janelia.saalfeldlab.n5.s3;

import java.io.IOException;

import org.janelia.saalfeldlab.n5.AbstractN5Test;
import org.janelia.saalfeldlab.n5.N5Writer;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.AnonymousAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import io.findify.s3mock.S3Mock;

/**
 * Initiates testing of the Amazon Web Services S3-based N5 implementation using S3 mock library.
 *
 * @author Igor Pisarev &lt;pisarevi@janelia.hhmi.org&gt;
 */
public class N5AmazonS3MockTest extends AbstractN5Test {

	static private String testBucketName = "n5-test";

	/**
	 * @throws IOException
	 */
	@Override
	protected N5Writer createN5Writer() throws IOException {

		final S3Mock api = new S3Mock.Builder().withPort(8001).withInMemoryBackend().build();
		api.start();

		final EndpointConfiguration endpoint = new EndpointConfiguration("http://localhost:8001", "us-west-2");

		final AmazonS3 s3 = AmazonS3ClientBuilder
			      .standard()
			      .withPathStyleAccessEnabled(true)
			      .withEndpointConfiguration(endpoint)
			      .withCredentials(new AWSStaticCredentialsProvider(new AnonymousAWSCredentials()))
			      .build();

		return new N5AmazonS3Writer(s3, testBucketName);
	}
}
