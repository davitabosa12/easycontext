package smd.ufc.br.easycontext.math;

import java.util.DoubleSummaryStatistics;

/**
 * Float version of Java 8's DoubleSummaryStatistics
 */
public class FloatStatistics {
    private long count;
    private float sum;
    private float sumCompensation; // Low order bits of sum
    private float simpleSum; // Used to compute right sum for non-finite inputs
    private float min = Float.POSITIVE_INFINITY;
    private float max = Float.NEGATIVE_INFINITY;

    public FloatStatistics() {
    }

    public void accept(float value) {
        ++count;
        simpleSum += value;
        sumWithCompensation(value);
        min = Math.min(min, value);
        max = Math.max(max, value);
    }

    public void combine(FloatStatistics other) {
        count += other.count;
        simpleSum += other.simpleSum;
        sumWithCompensation(other.sum);
        sumWithCompensation(other.sumCompensation);
        min = Math.min(min, other.min);
        max = Math.max(max, other.max);
    }

    private void sumWithCompensation(float value) {
        float tmp = value - sumCompensation;
        float velvel = sum + tmp; // Little wolf of rounding error
        sumCompensation = (velvel - sum) - tmp;
        sum = velvel;
    }

    public long getCount() {
        return count;
    }

    public float getSum() {
        return sum;
    }

    public float getMin() {
        return min;
    }

    public float getMax() {
        return max;
    }

    public float getAverage(){
        if(count <= 0) return 0.0f;
        return sum/count;
    }
}
