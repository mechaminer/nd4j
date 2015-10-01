/*
 *
 *  * Copyright 2015 Skymind,Inc.
 *  *
 *  *    Licensed under the Apache License, Version 2.0 (the "License");
 *  *    you may not use this file except in compliance with the License.
 *  *    You may obtain a copy of the License at
 *  *
 *  *        http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  *    Unless required by applicable law or agreed to in writing, software
 *  *    distributed under the License is distributed on an "AS IS" BASIS,
 *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *    See the License for the specific language governing permissions and
 *  *    limitations under the License.
 *
 *
 */

package org.nd4j.linalg.api.ops.impl.accum;

import org.apache.commons.math3.util.FastMath;
import org.nd4j.linalg.api.buffer.DataBuffer;
import org.nd4j.linalg.api.complex.IComplexNumber;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.api.ops.BaseAccumulation;
import org.nd4j.linalg.api.ops.Op;
import org.nd4j.linalg.api.parallel.bufferops.AccumulationDataBufferTask;
import org.nd4j.linalg.api.parallel.bufferops.impl.accum.Norm1OpDataBufferTask;
import org.nd4j.linalg.factory.Nd4j;

/**
 * Sum of absolute values
 *
 * @author Adam Gibson
 */
public class Norm1 extends BaseAccumulation {
    public Norm1() {
    }

    public Norm1(INDArray x, INDArray y, INDArray z, int n) {
        super(x, y, z, n);
    }

    public Norm1(INDArray x, INDArray y, int n) {
        super(x, y, n);
    }

    public Norm1(INDArray x) {
        super(x);
    }

    public Norm1(INDArray x, INDArray y) {
        super(x, y);
    }

    @Override
    public double update(double accum, double x){
        return (x>=0 ? accum+x : accum-x);
    }

    @Override
    public double update(double accum, double x, double y){
        return (x>=0 ? accum+x : accum-x);
    }

    @Override
    public float update(float accum, float x){
        return (x>=0 ? accum+x : accum-x);
    }

    @Override
    public float update(float accum, float x, float y){
        return (x>=0 ? accum+x : accum-x);
    }

    @Override
    public IComplexNumber update( IComplexNumber accum, double x){
        return accum.add(x>=0 ? x : -x);
    }

    @Override
    public IComplexNumber update( IComplexNumber accum, double x, double y){
        return accum.add(x>=0 ? x : -x);
    }

    @Override
    public IComplexNumber update( IComplexNumber accum, IComplexNumber x){
        return accum.add(x.absoluteValue());
    }

    @Override
    public IComplexNumber update( IComplexNumber accum, IComplexNumber x, IComplexNumber y){
        return accum.add(x.absoluteValue());
    }

    @Override
    public IComplexNumber update(IComplexNumber accum, IComplexNumber x, double y) {
        return accum.add(x.absoluteValue());
    }

    @Override
    public IComplexNumber zeroComplex() {
        return Nd4j.createComplexNumber(0.0, 0.0);
    }

    @Override
    public String name() {
        return "norm1";
    }

    @Override
    public Op opForDimension(int index, int dimension) {
        INDArray xAlongDimension = x.vectorAlongDimension(index, dimension);

        if (y() != null)
            return new Norm1(xAlongDimension, y.vectorAlongDimension(index, dimension), xAlongDimension.length());
        else
            return new Norm1(x.vectorAlongDimension(index, dimension));

    }

    @Override
    public Op opForDimension(int index, int... dimension) {
        INDArray xAlongDimension = x.tensorAlongDimension(index, dimension);

        if (y() != null)
            return new Norm1(xAlongDimension, y.tensorAlongDimension(index, dimension), xAlongDimension.length());
        else
            return new Norm1(x.tensorAlongDimension(index, dimension));
    }

    @Override
    public double combineSubResults(double first, double second){
        return first + second;
    }

    @Override
    public float combineSubResults(float first, float second){
        return first + second;
    }

    @Override
    public IComplexNumber combineSubResults(IComplexNumber first, IComplexNumber second){
        return first.add(second);
    }

    @Override
    public AccumulationDataBufferTask getAccumulationOpDataBufferTask(int threshold, int n, DataBuffer x, DataBuffer y,
                                                                      int offsetX, int offsetY, int incrX, int incrY, boolean outerTask){
        return new Norm1OpDataBufferTask(this,threshold,n,x,y,offsetX,offsetY,incrX,incrY,outerTask);
    }

    @Override
    public AccumulationDataBufferTask getAccumulationOpDataBufferTask(int tensorNum, int tensorDim, int parallelThreshold,
                                                                      INDArray x, INDArray y, boolean outerTask){
        return new Norm1OpDataBufferTask(this,tensorNum,tensorDim,parallelThreshold,x,y,outerTask);
    }
}
