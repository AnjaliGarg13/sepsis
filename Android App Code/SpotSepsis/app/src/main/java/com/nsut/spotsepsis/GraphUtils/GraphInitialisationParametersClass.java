package com.nsut.spotsepsis.GraphUtils;

import android.content.Context;
import android.graphics.Color;

import com.nsut.spotsepsis.models.GraphPointModel;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

public class GraphInitialisationParametersClass {

    private Context context;
    private LineChartView lineChartView;
    private List<PointValue> yAxisValues;

    public GraphInitialisationParametersClass(Context context, LineChartView lineChartView){
        this.context = context;
        this.lineChartView = lineChartView;
    }

    private int[] axisData = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24};
    private double[] yAxisData = {1, 5, 3, 2, 5, 1, 3, 3, 2, 5, 5, 1, 1.6, 0.8, 1, 2, 3, 4, 5, 1, 2, 3, 4, 5};

    public void plotData() {
        yAxisValues = new ArrayList<>();
        List axisValues = new ArrayList();

        Line line = new Line(yAxisValues)
                .setColor(Color.parseColor("#90CAF9"))
                .setPointColor(Color.parseColor("#90CAF9"))
                .setPointRadius(3);

        for (int i = 0; i < axisData.length; i++) {
            axisValues.add(i, new AxisValue(i).setLabel(String.valueOf(axisData[i])));
        }

        // Plot the graph data, retrieving from DB without updates.
        plotOnGraph(yAxisValues);

//        yAxisValues.add(new PointValue(1, (float) yAxisData[0]));
//        yAxisValues.add(new PointValue(5, (float) yAxisData[3]));
//        yAxisValues.add(new PointValue(8, (float) yAxisData[3]));
//        yAxisValues.add(new PointValue(14, (float) yAxisData[1]));
//        yAxisValues.add(new PointValue(15, (float) yAxisData[8]));
//        yAxisValues.add(new PointValue(18, (float) yAxisData[2]));
//        yAxisValues.add(new PointValue(24, (float) yAxisData[17]));

//        for (int i = 0; i < yAxisData.length; i++) {
//            yAxisValues.add(new PointValue(i, (float)yAxisData[i]));
//        }

        List<Line> lines = new ArrayList<>();
        lines.add(line);

        LineChartData data = new LineChartData();
        data.setLines(lines);

        Axis axis = new Axis();
        axis.setValues(axisValues);
        axis.setTextSize(14);
        axis.setTextColor(Color.parseColor("#BDBDBD"));
        data.setAxisXBottom(axis);

        Axis yAxis = new Axis();
//        yAxis.setName("Sales in millions");
        yAxis.setTextColor(Color.parseColor("#BDBDBD"));
        yAxis.setTextSize(14);
        data.setAxisYLeft(yAxis);

        lineChartView.setLineChartData(data);
        setViewPort();
    }

    private void plotOnGraph(List<PointValue> yAxisValues){
        ArrayList<GraphPointModel> graphPointsDTOArrayList = new ArrayList<>();

        graphPointsDTOArrayList.add(new GraphPointModel(2, 2));
        graphPointsDTOArrayList.add(new GraphPointModel(3, 1));
        graphPointsDTOArrayList.add(new GraphPointModel(4, 3));
        graphPointsDTOArrayList.add(new GraphPointModel(6, 4));
        graphPointsDTOArrayList.add(new GraphPointModel(8, 2));
        graphPointsDTOArrayList.add(new GraphPointModel(10, 3));
        graphPointsDTOArrayList.add(new GraphPointModel(11, 1));
        graphPointsDTOArrayList.add(new GraphPointModel(13, 4));
        graphPointsDTOArrayList.add(new GraphPointModel(15, 3));
        graphPointsDTOArrayList.add(new GraphPointModel(17, 5));
//        graphPointsDTOArrayList.add(new GraphPointModel(21, 4));
//        graphPointsDTOArrayList.add(new GraphPointModel(24, 1));

        for(GraphPointModel graphPointsDTO : graphPointsDTOArrayList){
            yAxisValues.add(new PointValue((float)graphPointsDTO.getxPoint(), (float)graphPointsDTO.getyPoint()));
        }
    }

    private void setViewPort(){
        Viewport viewport = new Viewport(lineChartView.getMaximumViewport());
        viewport.inset(-1, -1);
        viewport.top = 5;
        lineChartView.setMaximumViewport(viewport);
        lineChartView.setCurrentViewport(viewport);
        lineChartView.setZoomType(ZoomType.HORIZONTAL);
    }

//    public void updateGraph(){
//        ArrayList<GraphPointModel> graphPointsDTOArrayList = StoreData.retrieveGraphPointsDB(context);
//        graphPointsDTOArrayList.add(new GraphPointsDTO(1, 2, " Twitter"));
//        StoreData.updateGraphPointsDB(context, graphPointsDTOArrayList);
//
//        plotData();
//    }

}
