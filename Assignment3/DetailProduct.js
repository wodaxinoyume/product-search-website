import React, { useState } from 'react';
import { Container, Modal, Button, Image, Card, Row, Col } from 'react-bootstrap';
import { FaAngleLeft, FaAngleRight } from 'react-icons/fa';
import './App.css';

function PhotoModal(props) {
    const [currentImageIndex, setCurrentImageIndex] = useState(0);

    const previousImage = () => {
        if (currentImageIndex > 0) {
            setCurrentImageIndex(currentImageIndex - 1);
        }
    };

    const nextImage = () => {
        if (currentImageIndex < props.photoURL.length - 1) {
            setCurrentImageIndex(currentImageIndex + 1);
        }
    };

    if (!props.photoURL || props.photoURL.length === 0) {
        return;
    }

    return (
        <Modal
        {...props}
        size="md"
        aria-labelledby="contained-modal-title-vcenter"
        centered
        >
        <Modal.Header closeButton>
            <Modal.Title id="contained-modal-title-vcenter">
            Product Images
            </Modal.Title>
        </Modal.Header>
        <Modal.Body style={{ position: 'relative', textAlign: 'center' }}>
            <div style={{ display: 'inline-block', position: 'relative', marginTop: '0 20px' }}>
                <Image
                    src={props.photoURL[currentImageIndex]}
                    fluid
                    style={{ border: '10px solid #000'}}
                />
                <div style={{ position: 'absolute', top: '50%', transform: 'translateY(-50%)', left: 0, right: 0 }}>
                    <Button variant="secondary" style={{ position: 'absolute', left: 20 }} onClick={previousImage}>
                        <FaAngleLeft />
                    </Button>
                    <Button variant="secondary" style={{ position: 'absolute', right: 20 }} onClick={nextImage}>
                        <FaAngleRight />
                    </Button>
                </div>
            </div>
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