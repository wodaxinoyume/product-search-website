import React, { useState } from 'react';
import { Button } from 'react-bootstrap';
import AddShoppingCartIcon from '@mui/icons-material/AddShoppingCart';
import RemoveShoppingCartIcon from '@mui/icons-material/RemoveShoppingCart';

const FavoriteRenderer = (props) => {
    const [isFavorite, setIsFavorite] = useState(false);

    const toggleFavorite = () => {
        setIsFavorite(!isFavorite);
    };

    return (
        <Button variant='light' style={{ marginTop: "10px" }} onClick={toggleFavorite}>
            {isFavorite ? 
            <RemoveShoppingCartIcon style={{color: "rgb(178, 137, 12)"}}/> : 
            <AddShoppingCartIcon />}
        </Button>
    );
};

export default FavoriteRenderer;