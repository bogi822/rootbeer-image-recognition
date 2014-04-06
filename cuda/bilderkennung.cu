#include <stdio.h>
#include <stdlib.h>

#define NUMBER_OF_THREADS 100
#define NUMBER_OF_QUERY_IPS 100
#define NUMBER_OF_DATABASE_IPS 1000000

/**
 * This macro checks return value of the CUDA runtime call and exits
 * the application if the call failed.
 */
#define CUDA_CHECK_RETURN(value) {											\
	cudaError_t _m_cudaStat = value;										\
	if (_m_cudaStat != cudaSuccess) {										\
		fprintf(stderr, "Error %s at line %d in file %s\n",					\
				cudaGetErrorString(_m_cudaStat), __LINE__, __FILE__);		\
		exit(1);															\
	} }

__global__ void bruteForceMatcher(float *x1, float *y1, float *strength1, float *trace1, bool *sign1, float *scale1, float *descriptors1,
		float *x2, float *y2, float *strength2, float *trace2, bool *sign2, float *scale2, float *descriptors2, int *anzahlMatches) {

    float distance, delta;

    int blockSizeQuery = NUMBER_OF_QUERY_IPS / NUMBER_OF_THREADS;
    int blockSizeDB = NUMBER_OF_DATABASE_IPS / NUMBER_OF_THREADS;

    int startIndexQuery = blockIdx.x * blockSizeQuery;
    int endIndexQuery = blockIdx.x * blockSizeQuery + blockSizeQuery;

    int startIndexDB = blockIdx.x * blockSizeDB;
    int endIndexDB =  blockIdx.x * blockSizeDB + blockSizeDB;

	for(int i = startIndexQuery; i < endIndexQuery; i++) {
	    float bestDistance = 999999999.0f;
	    float secondBestDistance = 999999999.0f;
	    for (int a = startIndexDB; a < endIndexDB; a++) {
	        if (sign1[i] == sign2[a]){
	            distance = 0;
	            for (int b = 0; b < 64; b++) {
	                delta = descriptors1[b + (i * 64)] - descriptors2[b + (a * 64)];
	                distance += (delta * delta);
	            }
	            if(distance < bestDistance){
	                secondBestDistance = bestDistance;
	                bestDistance = distance;
	            }
	        }
	    }

	    // Ermittle die beste und die 2. beste distanz
	    if (bestDistance < 0.6f * secondBestDistance) {
	    	anzahlMatches[blockIdx.x]++;
	    }
	}
}

//__global__ void add(int *a, int *b, int *c, int **d) {
//	c[blockIdx.x] = a[blockIdx.x] + b[blockIdx.x];
//}

void random_ints(int* a, int anzahl){
   int i;
   for (i = 0; i < anzahl; ++i){
	  //a[i] = rand();
	  a[i] = 0;
   }
}

void random_floats(float* a, int anzahl){
   int i;
   for (i = 0; i < anzahl; ++i){
	  //a[i] = rand();
	  a[i] = 0.0005f;
   }
}

void random_bools(bool* a, int anzahl){
   int i;
   for (i = 0; i < anzahl; ++i){
	  a[i] = true;
   }
}

/**
 * Host function that prepares data array and passes it to the CUDA kernel.
 */
int main(void) {

	printf("Start ...\n");

	// Host variables
	float *x1, *y1, *strength1, *trace1, *scale1, *descriptors1, *x2, *y2, *strength2, *trace2, *scale2, *descriptors2;
	bool *sign1, *sign2;
	int *anzahlMatches;

	// Device variables
	float *d_x1, *d_y1, *d_strength1, *d_trace1, *d_scale1, *d_descriptors1, *d_x2, *d_y2, *d_strength2, *d_trace2, *d_scale2, *d_descriptors2;
	bool *d_sign1, *d_sign2;
	int *d_anzahlMatches;

	// Allocate cuda space
	printf("Start allocating space for cuda...\n");
	CUDA_CHECK_RETURN(cudaMalloc((void** ) &d_x1, NUMBER_OF_QUERY_IPS * sizeof(float)));
	CUDA_CHECK_RETURN(cudaMalloc((void** ) &d_y1, NUMBER_OF_QUERY_IPS * sizeof(float)));
	CUDA_CHECK_RETURN(cudaMalloc((void** ) &d_strength1, NUMBER_OF_QUERY_IPS * sizeof(float)));
	CUDA_CHECK_RETURN(cudaMalloc((void** ) &d_trace1, NUMBER_OF_QUERY_IPS * sizeof(float)));
	CUDA_CHECK_RETURN(cudaMalloc((void** ) &d_scale1, NUMBER_OF_QUERY_IPS * sizeof(float)));
	CUDA_CHECK_RETURN(cudaMalloc((void** ) &d_descriptors1, NUMBER_OF_QUERY_IPS * sizeof(float) * 64));
	CUDA_CHECK_RETURN(cudaMalloc((void** ) &d_sign1, NUMBER_OF_QUERY_IPS * sizeof(bool)));
	CUDA_CHECK_RETURN(cudaMalloc((void** ) &d_x2, NUMBER_OF_DATABASE_IPS * sizeof(float)));
	CUDA_CHECK_RETURN(cudaMalloc((void** ) &d_y2, NUMBER_OF_DATABASE_IPS * sizeof(float)));
	CUDA_CHECK_RETURN(cudaMalloc((void** ) &d_strength2, NUMBER_OF_DATABASE_IPS * sizeof(float)));
	CUDA_CHECK_RETURN(cudaMalloc((void** ) &d_trace2, NUMBER_OF_DATABASE_IPS * sizeof(float)));
	CUDA_CHECK_RETURN(cudaMalloc((void** ) &d_scale2, NUMBER_OF_DATABASE_IPS * sizeof(float)));
	CUDA_CHECK_RETURN(cudaMalloc((void** ) &d_descriptors2, NUMBER_OF_DATABASE_IPS * sizeof(float) * 64));
	CUDA_CHECK_RETURN(cudaMalloc((void** ) &d_sign2, NUMBER_OF_DATABASE_IPS * sizeof(bool)));
	CUDA_CHECK_RETURN(cudaMalloc((void** ) &d_anzahlMatches, NUMBER_OF_THREADS * sizeof(int)));
	printf("Allocating space for cuda finished!\n");

	// Create space for the query-data
	printf("Start allocating space for host...\n");
	x1 = (float *)malloc(NUMBER_OF_QUERY_IPS * sizeof(float)); random_floats(x1, NUMBER_OF_QUERY_IPS);
	y1 = (float *)malloc(NUMBER_OF_QUERY_IPS * sizeof(float)); random_floats(y1, NUMBER_OF_QUERY_IPS);
	strength1 = (float *)malloc(NUMBER_OF_QUERY_IPS * sizeof(float)); random_floats(strength1, NUMBER_OF_QUERY_IPS);
	trace1 = (float *)malloc(NUMBER_OF_QUERY_IPS * sizeof(float)); random_floats(trace1, NUMBER_OF_QUERY_IPS);
	scale1 = (float *)malloc(NUMBER_OF_QUERY_IPS * sizeof(float)); random_floats(scale1, NUMBER_OF_QUERY_IPS);
	descriptors1 = (float *)malloc(NUMBER_OF_QUERY_IPS * sizeof(float) * 64); random_floats(descriptors1, NUMBER_OF_QUERY_IPS * 64);
	sign1 = (bool *)malloc(NUMBER_OF_QUERY_IPS * sizeof(bool)); random_bools(sign1, NUMBER_OF_QUERY_IPS);

	x2 = (float *)malloc(NUMBER_OF_DATABASE_IPS * sizeof(float)); random_floats(x2, NUMBER_OF_DATABASE_IPS);
	y2 = (float *)malloc(NUMBER_OF_DATABASE_IPS * sizeof(float)); random_floats(y2, NUMBER_OF_DATABASE_IPS);
	strength2 = (float *)malloc(NUMBER_OF_DATABASE_IPS * sizeof(float)); random_floats(strength2, NUMBER_OF_DATABASE_IPS);
	trace2 = (float *)malloc(NUMBER_OF_DATABASE_IPS * sizeof(float)); random_floats(trace2, NUMBER_OF_DATABASE_IPS);
	scale2 = (float *)malloc(NUMBER_OF_DATABASE_IPS * sizeof(float)); random_floats(scale2, NUMBER_OF_DATABASE_IPS);
	descriptors2 = (float *)malloc(NUMBER_OF_DATABASE_IPS * sizeof(float) * 64); random_floats(descriptors2, NUMBER_OF_DATABASE_IPS * 64);
	sign2 = (bool *)malloc(NUMBER_OF_DATABASE_IPS * sizeof(bool)); random_bools(sign2, NUMBER_OF_DATABASE_IPS);
	anzahlMatches = (int *)malloc(NUMBER_OF_THREADS * sizeof(int));

	// Zero-fill the current match count
	for(int i = 0; i < NUMBER_OF_THREADS; i++){
		anzahlMatches[i] = 0;
	}

	printf("Start allocating space for host finished!\n");

	// Copy data to device
	printf("Start copying stuff to the gpu...\n");
	CUDA_CHECK_RETURN(cudaMemcpy(d_x1, x1, NUMBER_OF_QUERY_IPS * sizeof(float), cudaMemcpyHostToDevice));
	CUDA_CHECK_RETURN(cudaMemcpy(d_y1, y1, NUMBER_OF_QUERY_IPS * sizeof(float), cudaMemcpyHostToDevice));
	CUDA_CHECK_RETURN(cudaMemcpy(d_strength1, strength1, NUMBER_OF_QUERY_IPS * sizeof(float), cudaMemcpyHostToDevice));
	CUDA_CHECK_RETURN(cudaMemcpy(d_trace1, trace1, NUMBER_OF_QUERY_IPS * sizeof(float), cudaMemcpyHostToDevice));
	CUDA_CHECK_RETURN(cudaMemcpy(d_scale1, scale1, NUMBER_OF_QUERY_IPS * sizeof(float), cudaMemcpyHostToDevice));
	CUDA_CHECK_RETURN(cudaMemcpy(d_descriptors1, descriptors1, NUMBER_OF_QUERY_IPS * sizeof(float) * 64, cudaMemcpyHostToDevice));
	CUDA_CHECK_RETURN(cudaMemcpy(d_sign1, sign1, NUMBER_OF_QUERY_IPS * sizeof(bool), cudaMemcpyHostToDevice));
	CUDA_CHECK_RETURN(cudaMemcpy(d_x2, x2, NUMBER_OF_DATABASE_IPS * sizeof(float), cudaMemcpyHostToDevice));
	CUDA_CHECK_RETURN(cudaMemcpy(d_y2, y2, NUMBER_OF_DATABASE_IPS * sizeof(float), cudaMemcpyHostToDevice));
	CUDA_CHECK_RETURN(cudaMemcpy(d_strength2, strength2, NUMBER_OF_DATABASE_IPS * sizeof(float), cudaMemcpyHostToDevice));
	CUDA_CHECK_RETURN(cudaMemcpy(d_trace2, trace2, NUMBER_OF_DATABASE_IPS * sizeof(float), cudaMemcpyHostToDevice));
	CUDA_CHECK_RETURN(cudaMemcpy(d_scale2, scale2, NUMBER_OF_DATABASE_IPS * sizeof(float), cudaMemcpyHostToDevice));
	CUDA_CHECK_RETURN(cudaMemcpy(d_descriptors2, descriptors2, NUMBER_OF_DATABASE_IPS * sizeof(float) * 64, cudaMemcpyHostToDevice));
	CUDA_CHECK_RETURN(cudaMemcpy(d_sign2, sign2, NUMBER_OF_DATABASE_IPS * sizeof(bool), cudaMemcpyHostToDevice));
	CUDA_CHECK_RETURN(cudaMemcpy(d_anzahlMatches, anzahlMatches, NUMBER_OF_THREADS * sizeof(int), cudaMemcpyHostToDevice));
	printf("Copying stuff to the gpu finished!\n");

	printf("Start image-recognition ...\n");

	bruteForceMatcher<<<NUMBER_OF_THREADS, 1>>>(d_x1, d_y1, d_strength1, d_trace1, d_sign1, d_scale1, d_descriptors1,
								d_x2, d_y2, d_strength2, d_trace2, d_sign2, d_scale2, d_descriptors2, d_anzahlMatches);

	printf("Image-recognition ready!");

	CUDA_CHECK_RETURN(cudaThreadSynchronize());	// Wait for the GPU launched work to complete
	CUDA_CHECK_RETURN(cudaGetLastError());

	CUDA_CHECK_RETURN(cudaMemcpy(anzahlMatches, d_anzahlMatches, NUMBER_OF_THREADS * sizeof(int), cudaMemcpyDeviceToHost));

	for(int i = 0; i < NUMBER_OF_THREADS; i++){
		printf("Ergebnis: %d\n", anzahlMatches[i]);
	}

	CUDA_CHECK_RETURN(cudaFree(d_x1));
	CUDA_CHECK_RETURN(cudaFree(d_y1));
	CUDA_CHECK_RETURN(cudaFree(d_strength1));
	CUDA_CHECK_RETURN(cudaFree(d_trace1));
	CUDA_CHECK_RETURN(cudaFree(d_scale1));
	CUDA_CHECK_RETURN(cudaFree(d_descriptors1));
	CUDA_CHECK_RETURN(cudaFree(d_sign1));
	CUDA_CHECK_RETURN(cudaFree(d_x2));
	CUDA_CHECK_RETURN(cudaFree(d_y2));
	CUDA_CHECK_RETURN(cudaFree(d_strength2));
	CUDA_CHECK_RETURN(cudaFree(d_trace2));
	CUDA_CHECK_RETURN(cudaFree(d_scale2));
	CUDA_CHECK_RETURN(cudaFree(d_descriptors2));
	CUDA_CHECK_RETURN(cudaFree(d_sign2));

	CUDA_CHECK_RETURN(cudaDeviceReset());
	printf("End!\n");
	return 0;
}


