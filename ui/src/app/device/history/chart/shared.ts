import * as moment from 'moment';

export type Data = {
    labels: moment.Moment[],
    datasets: {
        backgroundColor: string,
        borderColor: string,
        data: number[],
        label: string,
        _meta: {}
    }[]
}

export type TooltipItem = {
    datasetIndex: number,
    index: number,
    x: number,
    xLabel: moment.Moment,
    y: number,
    yLabel: number
}

export type ChartOptions = {
    maintainAspectRatio: boolean,
    legend: {
        position: "bottom"
    },
    elements: {
        point: {
            radius: number,
            hitRadius: number,
            hoverRadius: number
        },
        line: {
            borderWidth: number
        }
    },
    scales: {
        yAxes: [{
            scaleLabel: {
                display: boolean,
                labelString: string
            },
            ticks: {
                beginAtZero: boolean,
                max?: number
            }
        }],
        xAxes: [{
            type: "time",
            time: {
                displayFormats: {
                    millisecond: string,
                    second: string,
                    minute: string,
                    hour: string,
                    day: string,
                    week: string,
                    month: string,
                    quarter: string,
                    year: string
                }
            }
        }]
    },
    tooltips: {
        mode: "label",
        callbacks: {
            label?(tooltipItem: TooltipItem, data: Data): string,
            title?(tooltipItems: TooltipItem[], data: Data): string
        }
    }
}

export const DEFAULT_TIME_CHART_OPTIONS: ChartOptions = {
    maintainAspectRatio: false,
    legend: {
        position: 'bottom'
    },
    elements: {
        point: {
            radius: 0,
            hitRadius: 10,
            hoverRadius: 10
        },
        line: {
            borderWidth: 1
        }
    },
    scales: {
        yAxes: [{
            scaleLabel: {
                display: true,
                labelString: ""
            },
            ticks: {
                beginAtZero: true
            }
        }],
        xAxes: [{
            type: 'time',
            time: {
                displayFormats: {
                    millisecond: 'SSS [ms]',
                    second: 'HH:mm:ss a', // 17:20:01
                    minute: 'HH:mm', // 17:20
                    hour: 'HH:mm', // 17:20
                    day: 'll', // Sep 4 2015
                    week: 'll', // Week 46, or maybe "[W]WW - YYYY" ?
                    month: 'MMM YYYY', // Sept 2015
                    quarter: '[Q]Q - YYYY', // Q3
                    year: 'YYYY' // 2015
                }
            }
        }]
    },
    tooltips: {
        mode: 'label',
        callbacks: {
            title(tooltipItems: TooltipItem[], data: Data): string {
                return tooltipItems[0].xLabel.format("LLLL");
            }
        }
    }
};
