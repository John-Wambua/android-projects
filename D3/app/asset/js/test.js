/*
*    test.js
*    Mastering Data Visualization with D3.js
*    Project 1 - Star Break Coffee
*/
const margin={ left:120,right:10,top:10,bottom:140};

const width=600-margin.left-margin.right;
const height=400-margin.top-margin.bottom;

let flag=true;

const t=d3.transition().duration(750);//Setting our transition variable; Value of the duration should be lower than our delay

const g=d3.select('#chart-area').append('svg')
    .attr('width',width+margin.left+margin.right)
    .attr('height',height+margin.top+margin.bottom)
    .append('g')
    .attr('transform','translate('+100+','+100+')')

const xAxisGroup=g.append('g')
    .attr('class','x axis')
    .attr('transform','translate(0,'+height+')');


const yAxisGroup= g.append('g')
    .attr('class','y axis');


// X-axis scale
const x=d3.scaleBand()

    .range([0,400])
    .paddingInner(0.3)
    .paddingOuter(0.3);

//Y axis scale
const y=d3.scaleLinear()
    .range([height,0]);

//x -axis LABEL
g.append('text')
    .attr('class','x axis-label')
    .attr('x',(width/2)-40)
    .attr('y',height+50)
    .attr('font-size','20px')
    .attr('font-weight',700)
    .attr('text-anchor','middle')
    .text('Month');

//y -axis LABEL
let yLabel=g.append('text')
    .attr('class','y-axis-label')
    .attr('x',-(height/2)-35)
    .attr('y',-60)
    .attr('font-weight',700)
    .attr('font-size','20px')
    .attr('transform','rotate(-90)')
    .text('Revenue');


d3.json('data/revenues.json').then(data=>{
    data.forEach(d=>{
        d.revenue=+d.revenue
        d.profit=+d.profit
    });

    console.log(data);

//CHANGE BETWEEN VIEWING THE REVENUES FOR PAST 6 MONTHS TO VIEWING PROFITS FOR PAST 6 MONTHS
    d3.interval(()=>{
        const newData=flag?data:data.slice(1);
        update(newData)
        flag=!flag;
    },1000);

    //Run the visualization for the first time
    update(data);
});



function update(data){

    const value=flag?'revenue':'profit';

    x.domain(data.map(d=>d.month));
    y.domain([0,d3.max(data,d=>d[value])]);

    //x-axis line
        const xAxisCall=d3.axisBottom(x);
        xAxisGroup.transition(t).call(xAxisCall);//set transition on x -axis

    //y-axis line
        const yAxisCall=d3.axisLeft(y);
        yAxisGroup.transition(t).call(yAxisCall);//set transition on y -axis



    /**Change all rectangles to circles to make a scatter plot**/

    //Join new Data with old elements
        const circles=g.selectAll('circle')
            .data(data,d=>d.month);
    //Exit old elements not present in new data

    circles.exit()
        //Add transitions to our exiting elements
        .attr('fill','red')
        .transition(t)
        .attr('cy',y(0))
        //.attr('height',0)//Gradually change height value to 0 so that it slides away from the screen
        .remove();



    //Update old elements present in new data
    //add our transitions before any of our elements are set
    circles.transition(t)
        .attr('cx',d=>x(d.month))
        .attr('cy',d=>y(d[value]))
        // .attr('width',x.bandwidth)
        // .attr('height',d=>height-y(d[value]))

    //Enter new elements present in new data
        circles.enter()
            .append('circle')
            .attr('cx',d=>x(d.month)+x.bandwidth()/2)
            .attr('cy',d=>y(0))
            // .attr('width',x.bandwidth)
            // .attr('height',0)
            .attr('fill','yellow')
    //Add transitions to the new bar to make them come from the ground up
            .transition(t)
            .attr('cy',d=>y(d[value]))
            .attr('r',5)
            // .attr('height',d=>height-y(d[value]))

    const label=flag?'Revenue':'Profit';
    yLabel.text(label);


}