import React from 'react';
import { Container, Row, Col, Card } from 'react-bootstrap';
import { CircularProgressbar } from 'react-circular-progressbar';
import { FcCheckmark } from 'react-icons/fc';
import { AiOutlineClose } from 'react-icons/ai';
import MdStars from '@mui/icons-material/Stars';
import MdStarRate from '@mui/icons-material/StarRate';
import 'react-circular-progressbar/dist/styles.css';
import './App.css';

const DetailSeller = (info, detailJson) => {

    const valueRenderer = (props) => {       
        if (props.name === 'Buy Product At') {
            return (
                <a href={props.value} target='_blank'>Store</a>
            );
        } else if (props.name === 'Popularity') {
            return (
                <div style={{ width: 50, height: 50 }}>
                    <CircularProgressbar value={props.value} maxValue={100} text={props.value} styles={{path : {stroke: "#008000"}}} />
                </div>    
            );
        } else if (props.name === "Top Rated") {
            if (props.value) {
                return <FcCheckmark style={{fontSize: "20px"}} />;
            } else {
                return <AiOutlineClose style={{color: "red", fontSize: "20px"}} />;
            }
        } else if (props.name === 'Feedback Rating Star') {
            if (props.value === "Yellow") {
                return <MdStarRate style={{color: "yellow", fontSize: "40px"}} />;
            } else if (props.value === "Blue") {
                return <MdStarRate style={{color: "blue", fontSize: "40px"}} />;
            } else if (props.value === "Turquoise") {
                return <MdStarRate style={{color: "#40E0D0", fontSize: "40px"}} />;
            } else if (props.value === "Purple") {
                return <MdStarRate style={{color: "purple", fontSize: "40px"}} />;
            } else if (props.value === "Red") {
                return <MdStarRate style={{color: "red", fontSize: "40px"}} />;
            } else if (props.value === "Green") {
                return <MdStarRate style={{color: "green", fontSize: "40px"}} />;
            } else if (props.value === "YellowShooting") {
                return <MdStars style={{color: "yellow", fontSize: "40px"}} />;
            } else if (props.value === "TurquoiseShooting") {
                return <MdStars style={{color: "#40E0D0", fontSize: "40px"}} />;
            } else if (props.value === "PurpleShooting") {
                return <MdStars style={{color: "purple", fontSize: "40px"}} />;
            } else if (props.value === "RedShooting") {
                return <MdStars style={{color: "red", fontSize: "40px"}} />;
            } else if (props.value === "GreenShooting") {
                return <MdStars style={{color: "green", fontSize: "40px"}} />;
            } else if (props.value === "SilverShooting") {
                return <MdStars style={{color: "silver", fontSize: "40px"}} />;
            }

            return props.value;
        }

        return props.value;
    }

    if (!info && !detailJson) {
        return null;
    }

    const rowData = [];

    if (info?.info?.[0]?.feedbackScore) {
        rowData.push({name: 'Feedback Score', value: info.info[0].feedbackScore})
    }
  
    if (info?.info?.[0]?.positiveFeedbackPercent) {
        rowData.push({name: 'Popularity', value: info.info[0].positiveFeedbackPercent })
    }
  
    if (info?.info?.[0]?.feedbackRatingStar?.[0]) {
        rowData.push({name: 'Feedback Rating Star', value: info.info[0].feedbackRatingStar[0]})
    }
  
    if (info?.info?.[0]?.topRatedSeller) {
        rowData.push({name: 'Top Rated', value: info.info[0].topRatedSeller === 'true' })
    }
  
    if (info?.detailJson?.[0]?.storeName) {
        rowData.push({name: 'Store Name', value: info.detailJson[0].storeName})
    }
  
    if (info?.detailJson?.[0]?.storeURL) {
        rowData.push({name: 'Buy Product At', value: info.detailJson[0].storeURL})
    }
 
    let mycolor1 = '#181d1f';
    let mycolor2 = '#222628';

    return (
        <Container style={{marginTop: "10px"}}>
            <Card style={{color: "white", backgroundColor: mycolor2, borderRadius: "0px", display: 'flex', alignItems: 'center', justifyContent: "center" }}>
                <Row style={{height: "60px"}}>
                    <Col style={{display: 'flex', alignItems: 'center', justifyContent: "center"}}>
                        <h4>{info?.info?.[0]?.sellerUserName ? info.info[0].sellerUserName : ""}</h4>
                    </Col>
                </Row>
            </Card>
            {rowData.map((item, index) => (
                <Card 
                key={index} 
                style={{ color: "white", 
                        backgroundColor: index % 2 === 0 ? mycolor1 : mycolor2, 
                        borderBottom: "0px solid #68686e",
                        borderRadius: "0px" }}>
                    <Row>
                        <Col lg={5} sm={12} style={{ marginLeft: "10px", height: item.name === "Popularity" ? "70px" : "50px", display: 'flex', alignItems: 'center'  }}>
                            {item.name}
                        </Col>
                        <Col lg={6} sm={12} style={{ marginLeft: "10px", height: item.name === "Popularity" ? "70px" : "50px", display: 'flex', alignItems: 'center'  }}>
                            {valueRenderer(item)}
                        </Col>
                    </Row>
                </Card>
            ))}
        </Container>
    );
}

export default DetailSeller;