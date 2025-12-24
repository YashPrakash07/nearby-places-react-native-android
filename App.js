import React,{useEffect,useState} from 'react';
import {ActivityIndicator,Alert} from 'react-native';
import MapView,{Marker} from 'react-native-maps';
import {NativeModules} from 'react-native';
const {PlacesModule}=NativeModules;
export default function App(){
 const [places,setPlaces]=useState([]);const [loading,setLoading]=useState(true);
 useEffect(()=>{PlacesModule.fetchNearbyPlaces().then(p=>{setPlaces(p);setLoading(false);}).catch(e=>{Alert.alert("Error",e.message||e);setLoading(false);});},[]);
 if(loading)return <ActivityIndicator style={{flex:1}}/>;
 return(<MapView style={{flex:1}}>{places.map(p=>(<Marker key={p.id} coordinate={{latitude:p.latitude,longitude:p.longitude}} title={p.name} description={(p.distance/1000).toFixed(2)+" km away"}/>))}</MapView>);
}