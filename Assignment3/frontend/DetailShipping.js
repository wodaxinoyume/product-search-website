import React from 'react';
import { Container, Col, Row, Card } from 'react-bootstrap';
import { FcCheckmark } from 'react-icons/fc';
import { AiOutlineClose } from 'react-icons/ai';
import './App.css';

const DetailShipping = (info, detailJson) => {

    if (!info && !detailJson) {
        return null;
    }

    const valueRenderer = (props) => {       
        if (props.name === 'Expedited Shipping' || props.name === 'One Day Shipping' || props.name === 'Return Accepted') {
            if (props.value) {
                return <FcCheckmark style={{fontSize: "20px"}} />;
            } else {
                return <AiOutlineClose style={{color: "red", fontSize: "20px"}} />;
            }
        }
        return props.value;
    }

    const rowData = [];

    if (info?.info?.[0]?.shippingServiceCost?.[0]?.__value__) {
        rowData.push({ name: 'Shipping Cost', value: info.info[0].shippingServiceCost[0].__value__ === "0.0" ? 'Free Shipping' : "$" + info.info[0].shippingServiceCost[0].__value__})
    }

    if (info?.info?.[0]?.shipToLocations?.[0]) {
        rowData.push({ name: 'Shipping Locations', value: info.info[0].shipToLocations[0] });
    }

    if (info?.info?.[0]?.handlingTime?.[0]) {
        let day = " Days";
        if (info.info[0].handlingTime[0] == "0" || info.info[0].handlingTime[0] === "1") {
            day = " Day";
        }
        rowData.push({ name: 'Handling time', value: info.info[0].handlingTime[0] + day });
    }

    if (info?.info?.[0]?.expeditedShipping?.[0]) {
        rowData.push({ name: 'Expedited Shipping', value: info.info[0].expeditedShipping[0] === 'true' });
    }

    if (info?.info?.[0]?.oneDayShippingAvailable?.[0]) {
        rowData.push({ name: 'One Day Shipping', value: info.info[0].oneDayShippingAvailable[0] === 'true' });
    }

    if (info?.detailJson?.[0]) {
        rowData.push({ name: 'Return Accepted', value: info.detailJson[0] === 'true' });
    }

    return (
        <Container style={{marginTop: "10px"}}>
            {rowData.map((item, index) => (
                <Card 
                key={index} 
                style={{ color: "white", 
                        backgroundColor: '#222628', 
                        borderBottom: "1px solid grey",
                        borderRadius: "0px" }}>
                    <Row style={{}}>
                        <Col lg={4} sm={12} style={{ marginLeft: "10px", height: "50px", display: 'flex', alignItems: 'center'  }}>
                            {item.name}
                        </Col>
                        <Col lg={7} sm={12} style={{ marginLeft: "10px", height: "50px", display: 'flex', alignItems: 'center'  }}>
                            {valueRenderer(item)}
                        </Col>
                    </Row>
                </Card>
            ))}
        </Container>
    );
}
  
export default DetailShipping;