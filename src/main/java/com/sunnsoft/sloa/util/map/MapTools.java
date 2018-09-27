package com.sunnsoft.sloa.util.map;

import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author 林宇民 Andy (llade)
 *
 */
public class MapTools {
	
	/**
	 * 判断是否在多边形范围内
	 * @param point 被测试的点
	 * @param polygonPoints 多边形各个顶点坐标（按连线顺序）。
	 * @return
	 */
	public static boolean isInPolygon(Point2D.Double point ,List<Point2D.Double> polygonPoints) {
		GeneralPath p = new GeneralPath();
		boolean isFirst = true;
		for (Iterator<Point2D.Double> iterator = polygonPoints.iterator(); iterator.hasNext();) {
			Point2D.Double pointOfPolygon = iterator.next();
			if(isFirst) {
				p.moveTo(pointOfPolygon.x, pointOfPolygon.y);  
			}else {
				p.lineTo(pointOfPolygon.x, pointOfPolygon.y); 
			}
			if(!iterator.hasNext()) {//最后一个点
				p.closePath(); 
			}
			isFirst = false;
		}
		return p.contains(point);
	}
	
	public static void main(String[] args) {
		//多边形坐标点
		Point2D.Double p1= new Point2D.Double(1,1);
		Point2D.Double p2= new Point2D.Double(2,3);
		Point2D.Double p3= new Point2D.Double(3,1);
		
		Point2D.Double p4= new Point2D.Double(2,2);//范围内的点坐标
		Point2D.Double p5= new Point2D.Double(1,2);//范围外的点坐标
		
		List<Point2D.Double> list= new ArrayList<>();
		list.add(p1);
		list.add(p2);
		list.add(p3);
		
		System.out.println(isInPolygon(p4, list));
		System.out.println(isInPolygon(p5, list));
	}

}
