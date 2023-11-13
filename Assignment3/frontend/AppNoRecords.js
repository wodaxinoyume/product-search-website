import React from 'react';
import { Card, Container } from 'react-bootstrap';
import './App.css';

const AppNoRecords = () => {
    return (
        <Container>
            <Card>
                <Card.Body className='noRecords'>No Records.</Card.Body>
            </Card>
        </Container>
    );
}

export default AppNoRecords;