import React, { useState } from 'react';
import { Button } from 'react-bootstrap';
import AddShoppingCartIcon from '@mui/icons-material/AddShoppingCart';
import RemoveShoppingCartIcon from '@mui/icons-material/RemoveShoppingCart';

const handleAdd = (object) => {
    object.title[0] = encodeURIComponent(object.title[0]);
    const jsonString = JSON.stringify(object);
    const encodedJsonString = encodeURIComponent(jsonString);

    const endpoint = `http://localhost:8080/addItem?object=${encodedJsonString}`;

    const url = new URL(endpoint);
    fetch(url)
        .then((response) => response.json())
        .then((data) => {
            console.log('add succeed');
        })
        .catch((error) => {
            console.error('Error fetching data:', error);
        });
}

const handleDelete = (object) => {

    const jsonString = JSON.stringify(object);
    const encodedJsonString = encodeURIComponent(jsonString);

    const endpoint = `http://localhost:8080/deleteItem?object=${encodedJsonString}`;

    const url = new URL(endpoint);
    fetch(url)
        .then((response) => response.json())
        .then((data) => {
            console.log('delete succeed');
        })
        .catch((error) => {
            console.error('Error fetching data:', error);
        });
}

const FavoriteRendererInWishlist = ({props, wishlistId, setWishlistId, setDeleteId, deleteId}) => {

    const [isFavorite, setIsFavorite] = useState(wishlistId.includes(parseInt(props[1][1], 10)));

    const toggleFavorite = () => {
        setIsFavorite(!isFavorite);
        
        const object = {
            "_id": parseInt(props[1][1], 10),
            "imageUrl": props[0],
            "title": props[1],
            "price": props[2],
            "cost": props[3],
        }

        const deleteObject = {
            "_id": parseInt(props[1][1], 10),
        }
        
        if (isFavorite) {
            handleDelete(deleteObject);
            setDeleteId(parseInt(props[1][1], 10)); // make wishlist refresh
            setWishlistId(wishlistId => wishlistId.filter(item => item !== parseInt(props[1][1], 10)));
            console.log(deleteId);
        } else {
            handleAdd(object);
            setWishlistId(wishlistId => [...wishlistId, parseInt(props[1][1], 10)]);
        }
    };

    return (
        <Button variant='light' style={{ marginTop: "10px" }} onClick={toggleFavorite}>
            {isFavorite ? 
            <RemoveShoppingCartIcon style={{color: "rgb(178, 137, 12)"}}/> : 
            <AddShoppingCartIcon />}
        </Button>
    );
};

export default FavoriteRendererInWishlist;