package io.openems.core.utilities;

import java.util.Collections;
import java.util.List;

public class ControllerUtils {

	public static double calculateCosPhi(long activePower, long reactivePower) {
		return Math.cos(Math.atan((double) activePower / (double) reactivePower));
	}

	public static long calculateReactivePower(long activePower, double cosPhi) {
		return (long) (Math.tan(Math.acos(cosPhi)) * activePower);
	}

	public static long calculateApparentPower(long activePower, long reactivePower) {
		return (long) Math.sqrt(Math.pow(activePower, 2) + Math.pow(reactivePower, 2));
	}

	public static long calculateActivePower(long apparentPower, double cosPhi) {
		return (long) (apparentPower * cosPhi);
	}

	public static long calculateApparentPower(long activePower, double cosPhi) {
		return (long) (activePower / cosPhi);
	}

	public static long reduceActivePower(long activePower, long reactivePower, long maxChargeApparentPower,
			long maxDischargeApparentPower) {
		boolean activePowerPos = true;

		if (activePower < 0) {
			activePowerPos = false;
		}

		if (isCharge(activePower, reactivePower)) {
			/*
			 * Charge
			 */
			if (calculateApparentPower(activePower, reactivePower) * -1 < maxChargeApparentPower) {
				double cosPhi = ControllerUtils.calculateCosPhi(activePower, reactivePower);
				activePower = ControllerUtils.calculateActivePower(maxChargeApparentPower, cosPhi);
			}
		} else {

			/*
			 * Discharge
			 */
			if (ControllerUtils.calculateApparentPower(activePower, reactivePower) > maxDischargeApparentPower) {
				double cosPhi = ControllerUtils.calculateCosPhi(activePower, reactivePower);
				activePower = ControllerUtils.calculateActivePower(maxDischargeApparentPower, cosPhi);
			}
		}
		if (!activePowerPos && activePower >= 0) {
			activePower *= -1;
		}

		return activePower;
	}

	public static long reduceReactivePower(long activePower, long reactivePower, long maxChargeApparentPower,
			long maxDischargeApparentPower) {
		boolean reactivePowerPos = true;
		if (reactivePower < 0) {
			reactivePowerPos = false;
		}
		if (isCharge(activePower, reactivePower)) {
			/*
			 * Charge
			 */
			if (calculateApparentPower(activePower, reactivePower) * -1 < maxChargeApparentPower) {
				double cosPhi = ControllerUtils.calculateCosPhi(activePower, reactivePower);
				reactivePower = ControllerUtils.calculateReactivePower(reduceActivePower(activePower, reactivePower,
						maxChargeApparentPower, maxDischargeApparentPower), cosPhi);
			}
		} else {

			/*
			 * Discharge
			 */
			if (ControllerUtils.calculateApparentPower(activePower, reactivePower) > maxDischargeApparentPower) {
				double cosPhi = ControllerUtils.calculateCosPhi(activePower, reactivePower);
				reactivePower = ControllerUtils.calculateReactivePower(reduceActivePower(activePower, reactivePower,
						maxChargeApparentPower, maxDischargeApparentPower), cosPhi);
			}
		}
		if (!reactivePowerPos && reactivePower >= 0) {
			reactivePower *= -1;
		}
		return reactivePower;
	}

	public static boolean isCharge(long activePower, long reactivePower) {
		if (activePower >= 0 && reactivePower >= 0) {
			return false;
		} else if (activePower < 0 && reactivePower >= 0) {
			return true;
		} else if (activePower < 0 && reactivePower < 0) {
			return true;
		} else {
			return false;
		}
	}

	public static Long getValueOfLine(List<Point> points, long x) {
		Point smaller = getSmallerPoint(points, x);
		Point greater = getGreaterPoint(points, x);
		if (smaller != null && greater == null) {
			return smaller.y;
		} else if (smaller == null && greater != null) {
			return greater.y;
		} else if (smaller != null && greater != null) {
			double m = (greater.y - smaller.y) / (greater.x - smaller.x);
			double t = smaller.y - m * smaller.x;
			return (long) (m * x + t);
		} else {
			throw new ArithmeticException("x[" + x + "] is out of Range of the points");
		}
	}

	private static Point getGreaterPoint(List<Point> points, long x) {
		Collections.sort(points, (p1, p2) -> (int) (p1.x - p2.x));
		for (int i = 0; i < points.size(); i++) {
			Point p = points.get(i);
			if (p.x > x) {
				return p;
			}
		}
		return null;
	}

	private static Point getSmallerPoint(List<Point> points, long x) {
		Collections.sort(points, (p1, p2) -> (int) (p2.x - p1.x));
		for (int i = 0; i < points.size(); i++) {
			Point p = points.get(i);
			if (p.x < x) {
				return p;
			}
		}
		return null;
	}
}