import React, { useState } from 'react';
import { Container, Modal, Button, Card, Row, Col, Carousel } from 'react-bootstrap';
import { FaAngleLeft, FaAngleRight } from 'react-icons/fa';
import './App.css';

function PhotoModal(props) {
    const [index, setIndex] = useState(0);

    const handleSelect = (selectedIndex) => {
        setIndex(selectedIndex);
    };

    if (!props.photoURL || props.photoURL.length === 0) {
        return;
    }

    return (
        <Modal {...props} size="md" aria-labelledby="contained-modal-title-vcenter" centered>
            <Modal.Header closeButton>
                <Modal.Title id="contained-modal-title-vcenter">
                Product Images
                </Modal.Title>
            </Modal.Header>
            <Modal.Body style={{ textAlign: 'center' }}>
                <Carousel activeIndex={index} onSelect={handleSelect} 
                            style={{ width: "100%", height: "400px", display: "flex", justifyContent: "center", alignItems: "center" }}>
                    {props.photoURL.map((imageSrc, i) => (
                        <Carousel.Item key={i}>
                            <a href={imageSrc} target='_blank'>
                                <img src={imageSrc} alt={`Image ${i}`} style={{ border: '10px solid #000', maxWidth: "100%", maxHeight: "400px" }} />
                            </a>
                        </Carousel.Item>
                    ))}
                </Carousel>
            </Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={props.onHide}>Close</Button>
            </Modal.Footer>
        </Modal>
    );
}

const DetailProduct = ({detailJson}) => {
    
    const [modalShow, setModalShow] = useState(false);

    const titleRenderer = (props) => {       
        if (props.name === 'Product Images') {
            return (
                <a className='itemTitle' onClick={() => setModalShow(true)}>
                    View Product Images Here
                </a>
            );
        }

        return props.value;
    }

    const rowData = [];

    if (detailJson?.Item?.PictureURL?.length > 0) {
        rowData.push({ name: 'Product Images', value: "" });
    }

    if (detailJson?.Item?.ConvertedCurrentPrice?.Value) {
        rowData.push({ name: 'Price', value: "$" + detailJson.Item.ConvertedCurrentPrice.Value });
    }

    if (detailJson?.Item?.Location) {
        rowData.push({ name: 'Location', value: detailJson.Item.Location });
    }

    if (detailJson?.Item?.ReturnPolicy?.ReturnsAccepted || detailJson?.Item?.ReturnPolicy?.ReturnsWithin) {
        if (!detailJson?.Item?.ReturnPolicy?.ReturnsAccepted) {
            rowData.push({ name: 'Return Policy (US)', value: detailJson.Item.ReturnPolicy.ReturnsWithin });
        } else if (!detailJson?.Item?.ReturnPolicy?.ReturnsWithin) {
            rowData.push({ name: 'Return Policy (US)', value: detailJson.Item.ReturnPolicy.ReturnsAccepted });
        } else {
            rowData.push({ name: 'Return Policy (US)', value: detailJson.Item.ReturnPolicy.ReturnsAccepted + " within " + detailJson.Item.ReturnPolicy.ReturnsWithin });
        }
    }

    const itemSpecifics = detailJson?.Item?.ItemSpecifics?.NameValueList;
    if (itemSpecifics) {
        rowData.push(
            ...itemSpecifics.map(item => ({
                name: item.Name,
                value: item.Value.join(', '),
            }))
        );
    }

    let mycolor1 = '#181d1f';
    let mycolor2 = '#222628';
        
    return (
        <Container style={{marginTop: "10px"}}>
            {rowData.map((item, index) => (
                <Card 
                key={index} 
                style={{ color: "white", 
                        backgroundColor: index % 2 === 0 ? mycolor1 : mycolor2, 
                        borderBottom: "0px solid #68686e",
                        borderRadius: "0px" }}>
                    <Row style={{}}>
                        <Col lg={4} sm={12} style={{ marginLeft: "10px", height: "50px", display: 'flex', alignItems: 'center'  }}>
                            {item.name}
                        </Col>
                        <Col lg={7} sm={12} style={{ marginLeft: "10px", height: "50px", display: 'flex', alignItems: 'center', whiteSpace: 'nowrap', overflow: 'hidden', textOverflow: 'ellipsis'}}>
                            {titleRenderer(item)}
                        </Col>
                    </Row>
                </Card>
            ))}
            <PhotoModal
                show={modalShow}
                onHide={() => setModalShow(false)}
                photoURL={detailJson?.Item?.PictureURL}
            />
        </Container>
    );
}

export default DetailProduct;