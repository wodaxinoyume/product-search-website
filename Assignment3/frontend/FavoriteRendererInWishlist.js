import React, { useState } from 'react';
import { Button } from 'react-bootstrap';
import AddShoppingCartIcon from '@mui/icons-material/AddShoppingCart';
import RemoveShoppingCartIcon from '@mui/icons-material/RemoveShoppingCart';

const handleDelete = (object, setDeleteId) => {

    const jsonString = JSON.stringify(object);
    const encodedJsonString = encodeURIComponent(jsonString);

    // const endpoint = `http://localhost:8080/deleteItem?object=${encodedJsonString}`;
    const endpoint = `https://rugged-shuttle-402803.wn.r.appspot.com/deleteItem?object=${encodedJsonString}`;

    const url = new URL(endpoint);
    fetch(url)
        .then((response) => response.json())
        .then((data) => {
            console.log('delete succeed');
            setDeleteId("success");
        })
        .catch((error) => {
            console.error('Error fetching data:', error);
        });
}

const FavoriteRendererInWishlist = ({props, wishlistId, setWishlistId, setDeleteId, deleteId}) => {

    const [isFavorite, setIsFavorite] = useState(wishlistId.includes(parseInt(props[1][1], 10)));

    const toggleFavorite = () => {
        setIsFavorite(false);
        setDeleteId(parseInt(props[1][1], 10)); // make wishlist refresh
        
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
            handleDelete(deleteObject, setDeleteId);
            setWishlistId(wishlistId => wishlistId.filter(item => item !== parseInt(props[1][1], 10)));
            console.log(deleteId);
        } // do nothing because no need to add!
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