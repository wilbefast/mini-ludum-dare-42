package com.jackamikaz.gameengine.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.g3d.model.Model;
import com.badlogic.gdx.graphics.g3d.model.SubMesh;
import com.badlogic.gdx.graphics.g3d.model.skeleton.Skeleton;
import com.badlogic.gdx.graphics.g3d.model.skeleton.SkeletonAnimation;
import com.badlogic.gdx.graphics.g3d.model.skeleton.SkeletonJoint;
import com.badlogic.gdx.graphics.g3d.model.skeleton.SkeletonKeyframe;
import com.badlogic.gdx.graphics.g3d.model.skeleton.SkeletonModel;
import com.badlogic.gdx.graphics.g3d.model.skeleton.SkeletonSubMesh;
import com.badlogic.gdx.graphics.g3d.model.still.StillModel;
import com.badlogic.gdx.graphics.g3d.model.still.StillSubMesh;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public class GmfLoader {
	
	private static void getScaleFromMatrix(Matrix4 mat, Vector3 scale) {
		
		float mul = (mat.det() < 0) ? -1.0f : 1.0f;
		
	//	float sx = scale.set(mat.val[Matrix4.M00],mat.val[Matrix4.M01],mat.val[Matrix4.M02]).len();
	//	float sy = scale.set(mat.val[Matrix4.M10],mat.val[Matrix4.M11],mat.val[Matrix4.M12]).len();
	//	float sz = scale.set(mat.val[Matrix4.M20],mat.val[Matrix4.M21],mat.val[Matrix4.M22]).len();
		
		float sx = mul * scale.set(mat.val[Matrix4.M00],mat.val[Matrix4.M10],mat.val[Matrix4.M20]).len();
		float sy = mul * scale.set(mat.val[Matrix4.M01],mat.val[Matrix4.M11],mat.val[Matrix4.M21]).len();
		float sz = mul * scale.set(mat.val[Matrix4.M02],mat.val[Matrix4.M12],mat.val[Matrix4.M22]).len();
		
		scale.set(sx,sy,sz);
	}
	
	public static Model loadGmf(InputStream in) {
		Skeleton skel = new Skeleton();
		SubMesh subMeshes[] = null;
		int numSubMeshes = 0;
		
		boolean hasSkeleton = false;
		boolean hasAnimations = false;
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line;
		try {
			line = reader.readLine();
			while (line != null) {
				String[] tokens = line.split("[ ]+");
				
				if (tokens[0].equalsIgnoreCase("Model")) {
					numSubMeshes = Integer.parseInt(tokens[1]);
					subMeshes = new SubMesh[numSubMeshes];
				}
				
				for(int i=0; i<numSubMeshes; ++i) {
					if (tokens[0].equalsIgnoreCase("Mesh"))
						subMeshes[i] = extractMesh(reader, tokens[1], tokens[2].equalsIgnoreCase("hasSkin"));
				}
				
				if (tokens[0].equalsIgnoreCase("Skeleton")) {
					int numBones = Integer.parseInt(tokens[1]);
					extractSkeleton(reader, numBones, skel);
					hasSkeleton = true;
				}
				
				if (tokens[0].equalsIgnoreCase("Animations")) {
					int numAnims = Integer.parseInt(tokens[1]);
					extractAnimations(reader, numAnims, skel);
					hasAnimations = true;
				}
				
				line = reader.readLine();
			}
			
			reader.close();
		} catch (IOException e) {
			return null;
		}
		
		//Vector3 v = new Vector3();
		if (hasSkeleton && hasAnimations) {
			for(SkeletonAnimation anim : skel.animations.values()) {
				int i=0;
				for(SkeletonKeyframe[] channel : anim.perJointkeyFrames) {
					for(SkeletonKeyframe key : channel) {
						SkeletonKeyframe base = skel.bindPoseJoints.get(i);
						
						//v.set(key.position);
						
						base.rotation.transform(key.position);
						key.position.mul(base.scale);
						key.position.add(base.position);
						
						key.rotation.mulLeft(base.rotation);
						key.scale.mul(base.scale);
						
					//	key.position.sub(base.position);
					//	key.rotation.mulLeft(base.rotation);
					//	key.scale.div(base.scale);
					}
					
					++i;
				}
			}
			
			return new SkeletonModel(skel, subMeshes);
		}
		else {
			return new StillModel(subMeshes);
		}
	}
	
	private static void extractAnimations(BufferedReader reader, int amount, Skeleton skel) throws IOException {
		
		String line;
		for (int i=0; i<amount; ++i) {
			line = reader.readLine();
			String[] tokens = line.split("[ ]+");
			
			skel.animations.put(tokens[1], extractAnimation(reader, tokens[1], Integer.parseInt(tokens[2])));
		}
		
	}
	
	private static SkeletonAnimation extractAnimation(BufferedReader reader, String name, int amount) throws IOException {
		SkeletonKeyframe[][] perJointKeyFrames = new SkeletonKeyframe[amount][];
		
		float tmax = -1.0f;
		
		String line;
		for (int i=0; i<amount; ++i) {
			line = reader.readLine();
			String[] tokens = line.split("[ ]+");
			
			int channelLength = Integer.parseInt(tokens[2]);
			SkeletonKeyframe[] channel = extractChannel(reader, channelLength);
			
			if (channelLength > 0 && channel[channelLength-1].timeStamp > tmax)
				tmax = channel[channelLength-1].timeStamp;
				
			perJointKeyFrames[i] = channel;
		}
		
		return new SkeletonAnimation(name, tmax, perJointKeyFrames);
	}
	
	private static SkeletonKeyframe[] extractChannel(BufferedReader reader, int amount) throws IOException {
		SkeletonKeyframe[] ret = new SkeletonKeyframe[amount];
		
		String line;
		for (int i=0; i<amount; ++i) {
			line = reader.readLine();
			String[] tokens = line.split("[ ]+");
			
			ret[i] = extractKeyframe(tokens);
		}
		
		return ret;
	}
	
	private static void extractSkeleton(BufferedReader reader, int amount, Skeleton skel) throws IOException {
		SkeletonJoint[] joints = new SkeletonJoint[amount];
		
		String line;
		for (int i=0; i<amount; ++i) {
			line = reader.readLine();
			String[] tokens = line.split("[ ]+");
			joints[i] = extractJoint(tokens);
		}
		
		//the transforms are global ones, make them as local
		Matrix4 globalMat[] = new Matrix4[amount];
		Matrix4 invGlobalMat[] = new Matrix4[amount];

		for(int i=0; i<amount; ++i) {
			Matrix4 mat = new Matrix4();
			joints[i].rotation.toMatrix(mat.val);
			mat.trn(joints[i].position).scl(joints[i].scale);
			globalMat[i] = mat;
			invGlobalMat[i] = new Matrix4(mat).inv();
		}
		
		//compute the locals
		Matrix4 localMat = new Matrix4();
		for(int i=0; i<amount; ++i) {
			
			if(joints[i].parentIndex >= 0)
				localMat.set(invGlobalMat[joints[i].parentIndex]).mul(globalMat[i]);
			else
				localMat.set(globalMat[i]);
			
			//store it back in correct format
			localMat.getTranslation(joints[i].position);
			localMat.getRotation(joints[i].rotation);
			getScaleFromMatrix(localMat,joints[i].scale);
		}
		
		// build the hierarchy
		for (int i=0; i<amount; ++i) {
			SkeletonJoint current = joints[i];
			if (current.parentIndex < 0)
				skel.hierarchy.add(current);
			else {
				current.parent = joints[current.parentIndex];
				current.parent.children.add(current);
			}
		}
		
		skel.buildFromHierarchy();
	}
	
	private static SkeletonJoint extractJoint(String[] tokens) {
		SkeletonJoint joint = new SkeletonJoint();
		
		joint.name = tokens[0];
		joint.parentIndex = Integer.parseInt(tokens[1]);
		
		joint.position.x = Float.parseFloat(tokens[2]);
		joint.position.y = Float.parseFloat(tokens[3]);
		joint.position.z = Float.parseFloat(tokens[4]);
		
		joint.rotation.w = Float.parseFloat(tokens[5]);
		joint.rotation.x = Float.parseFloat(tokens[6]);
		joint.rotation.y = Float.parseFloat(tokens[7]);
		joint.rotation.z = Float.parseFloat(tokens[8]);
		
		joint.scale.x = Float.parseFloat(tokens[9]);
		joint.scale.y = Float.parseFloat(tokens[10]);
		joint.scale.z = Float.parseFloat(tokens[11]);
		
		return joint;
	}
	
	private static SkeletonKeyframe extractKeyframe(String[] tokens) {
		SkeletonKeyframe key = new SkeletonKeyframe();
		
		key.timeStamp = Float.parseFloat(tokens[0]);
		
		key.position.x = Float.parseFloat(tokens[1]);
		key.position.y = Float.parseFloat(tokens[2]);
		key.position.z = Float.parseFloat(tokens[3]);
		
		key.rotation.w = Float.parseFloat(tokens[4]);
		key.rotation.x = Float.parseFloat(tokens[5]);
		key.rotation.y = Float.parseFloat(tokens[6]);
		key.rotation.z = Float.parseFloat(tokens[7]);
		
		key.scale.x = Float.parseFloat(tokens[8]);
		key.scale.y = Float.parseFloat(tokens[9]);
		key.scale.z = Float.parseFloat(tokens[10]);
		
		return key;
	}
	
	private static SubMesh extractMesh(BufferedReader reader, String name, boolean hasSkin) throws IOException {
		
		float vertices[] = null;
		short indices[] = null;
		
		int numVertices= 0;
		int numTriangles = 0;
		
		int[][] boneAssignments = null;
		float[][] boneWeights = null;
		
		String line;
		for(int i=0; i < (hasSkin ? 3 : 2); ++i) {
			line = reader.readLine();
			String[] tokens = line.split("[ ]+");
			
			if (tokens[0].equalsIgnoreCase("Vertices")) {
				numVertices = Integer.parseInt(tokens[1]);
				vertices = extractVertices(reader, numVertices);
			} else if (tokens[0].equalsIgnoreCase("Triangles")) {
				numTriangles = Integer.parseInt(tokens[1]);
				indices = extractTriangles(reader, numTriangles);
			} else if (tokens[0].equalsIgnoreCase("Skin")) {
				int numSkinnedVertices = Integer.parseInt(tokens[1]);
				
				boneAssignments = new int[numSkinnedVertices][];
				boneWeights = new float[numSkinnedVertices][];
				
				extractSkin(reader, numSkinnedVertices, boneAssignments, boneWeights);
			}
		}
		
		Mesh mesh = new Mesh(false,numVertices,numTriangles*3,
				VertexAttribute.Position(),
				VertexAttribute.Normal(),
				VertexAttribute.TexCoords(0));
		mesh.setVertices(vertices);
		mesh.setIndices(indices);
		
		if (hasSkin) {
			SkeletonSubMesh subMesh = new SkeletonSubMesh(name, mesh, GL10.GL_TRIANGLES);
			subMesh.setVertices(vertices);
			subMesh.setIndices(indices);
			subMesh.skinnedVertices = new float[vertices.length];
			System.arraycopy(subMesh.vertices, 0, subMesh.skinnedVertices, 0, subMesh.vertices.length);
			subMesh.boneAssignments = boneAssignments;
			subMesh.boneWeights = boneWeights;
			return subMesh;
		}
		else {
			return new StillSubMesh(name, mesh, GL10.GL_TRIANGLES);
		}
		
	}
	
	private static void extractSkin(BufferedReader reader, int amount, int boneAssignments[][], float boneWeights[][]) throws IOException {
		
		String line;
		for (int i=0; i<amount; ++i) {
			line = reader.readLine();
			String[] tokens = line.split("[ ]+");
			
			int numInfluences = Integer.parseInt(tokens[1]);
			boneAssignments[i] = new int[numInfluences];
			boneWeights[i] = new float[numInfluences];
			
			for(int j=0; j<numInfluences; ++j) {
				boneAssignments[i][j] = Integer.parseInt(tokens[2 + 2*j]);
				boneWeights[i][j] = Float.parseFloat(tokens[2 + 2*j +1]);
			}
		}
	}
	
	private static float[] extractVertices(BufferedReader reader, int amount) throws IOException {
		float[] ret = new float[amount*8];
		
		String line;
		for (int i=0; i<amount; ++i) {
			line = reader.readLine();
			String[] tokens = line.split("[ ]+");
			
			for (int j=0; j<8; ++j) {
				ret[i*8 + j] = Float.parseFloat(tokens[j]);
			}
			
		}
		
		return ret;
	}
	
	private static short[] extractTriangles(BufferedReader reader, int amount) throws IOException {
		short[] ret = new short[amount*3];
		
		String line;
		for (int i=0; i<amount; ++i) {
			line = reader.readLine();
			String[] tokens = line.split("[ ]+");
			
			for (int j=0; j<3; ++j) {
				ret[i*3 + j] = Short.parseShort(tokens[j]);
			}
			
		}
		
		return ret;
	}
	
}
