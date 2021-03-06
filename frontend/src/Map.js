import React, { useEffect, useState } from "react";
import axios from "axios";
import Route from "./Route";
import nearest from "./axios/nearest";

const Map = ({ corners, canvasRef, onZoom, setError }) => {
  const { se, nw } = corners;
  const [CANVAS_WIDTH, CANVAS_HEIGHT] = [600, 600];

  const TILE_ORIGIN = {
    latitude: 41.82686216366997,
    longitude: -71.40645750837325,
  };
  const TILE_WIDTH = (-1 * (71.4000750453612 - 71.40645750837325)) / 2;
  const TILE_HEIGHT = (41.82686216366997 - 41.82355259953458) / 2;

  const [activeWays, setActiveWays] = useState([]);

  const [pointOne, setPointOne] = useState(null);
  const [pointTwo, setPointTwo] = useState(null);

  const [activeRoute, setActiveRoute] = useState([]);

  const getTileFromCoords = (latitude, longitude) => {
    const tilesHeight = (latitude - TILE_ORIGIN.latitude) / TILE_HEIGHT;
    const tilesWidth = (longitude - TILE_ORIGIN.longitude) / TILE_WIDTH;

    let tileLong;
    let tileLat;
    if (tilesHeight == 0) {
      tileLong = TILE_ORIGIN.longitude;
    } else {
      tileLong =
        TILE_ORIGIN.longitude +
        ((TILE_WIDTH * tilesWidth) / Math.abs(tilesWidth)) *
          Math.floor(Math.abs(tilesWidth));
    }

    if (tilesWidth == 0) {
      tileLat = TILE_ORIGIN.latitude;
    } else {
      tileLat =
        TILE_ORIGIN.latitude +
        ((TILE_HEIGHT * tilesHeight) / Math.abs(tilesHeight)) *
          Math.floor(Math.abs(tilesHeight));
    }

    return { latitude: tileLat, longitude: tileLong };
  };

  const getActiveTiles = () => {
    const tileNW = getTileFromCoords(nw.latitude, nw.longitude);
    const tileSE = getTileFromCoords(se.latitude, se.longitude);

    let currentTile = {
      latitude: tileNW.latitude + TILE_HEIGHT,
      longitude: tileNW.longitude - TILE_WIDTH,
    };

    const activeTiles = [];

    while (currentTile.latitude > tileSE.latitude - TILE_HEIGHT) {
      while (currentTile.longitude < tileSE.longitude + TILE_WIDTH) {
        activeTiles.push(currentTile);
        const newTile = {
          latitude: currentTile.latitude,
          longitude: currentTile.longitude + TILE_WIDTH,
        };
        currentTile = newTile;
      }
      currentTile.longitude = tileNW.longitude - TILE_WIDTH;
      currentTile.latitude = currentTile.latitude - TILE_HEIGHT;
    }

    return activeTiles;
  };

  const getPointFromCoords = (point) => {
    const latRange = nw.latitude - se.latitude;
    const longRange = se.longitude - nw.longitude;

    const startX =
      (CANVAS_WIDTH * (point.longitude - nw.longitude)) / longRange;
    const startY =
      (CANVAS_HEIGHT * -1 * (point.latitude - nw.latitude)) / latRange;

    return { x: startX, y: startY };
  };

  const getPointsFromWay = (n1, n2) => {
    const start = getPointFromCoords(n1);

    const end = getPointFromCoords(n2);

    return { start, end };
  };

  const draw = (context) => {
    context.canvas.width = CANVAS_WIDTH;
    context.canvas.height = CANVAS_HEIGHT;
    context.fillStyle = "#2ca25f";
    context.fillRect(0, 0, context.canvas.width, context.canvas.height);
  };

  const drawWay = (context, way) => {
    const { start, end } = getPointsFromWay(way.start, way.end);
    context.moveTo(start.x, start.y);
    context.lineTo(end.x, end.y);
  };

  const requestWays = (tile) => {
    axios
      .post(
        "http://localhost:4567/ways",
        {
          nwLat: tile.latitude,
          nwLong: tile.longitude,
          seLat: tile.latitude - TILE_HEIGHT,
          seLong: tile.longitude + TILE_WIDTH,
        },
        {
          headers: {
            "Content-Type": "application/json",
            "Access-Control-Allow-Origin": "*",
          },
        }
      )
      .then((res) => {
        const ways = res.data.ways;
        const tileId = JSON.stringify(tile);
        localStorage.setItem(tileId, JSON.stringify(ways));
        setActiveWays((actWays) => {
          return actWays.concat(ways);
        });
      })
      .catch((err) => {
          setError("Error: could not retrieve ways from server")
      });
  };

  const onDoubleClick = (e) => {
    const canvas = canvasRef.current;
    const rect = canvas.getBoundingClientRect();
    const x = e.pageX - rect.x;
    const y = e.pageY - rect.y;

    if (x > 0 && y > 0) {
      const latRange = nw.latitude - se.latitude;
      const longRange = se.longitude - nw.longitude;

      const lat = nw.latitude - (latRange * y) / CANVAS_HEIGHT;
      const long = nw.longitude + (longRange * x) / CANVAS_WIDTH;

      const onFoundNearest = (res) => {
        const { coords, id } = res.data;
        if (!pointTwo && !pointOne) {
          setPointOne({
            coords: { latitude: coords[0], longitude: coords[1] },
            id,
          });
        } else {
          if (pointOne && pointTwo) {
            setActiveRoute([]);
            setPointTwo(null);
            setPointOne({
              coords: { latitude: coords[0], longitude: coords[1] },
              id,
            });
          } else if (pointOne && !pointTwo) {
            //WHERE ROUTE CALL GOES
            setPointTwo({
              coords: { latitude: coords[0], longitude: coords[1] },
              id,
            });
          } else if (!pointOne && pointTwo) {
            //WHERE ROUTE CALL GOES
            setPointOne({
              coords: { latitude: coords[0], longitude: coords[1] },
              id,
            });
          }
        }
      };

      const error = () => {
          setError("Error: Could not retrieve nearest node from server")
      }
      nearest(lat, long, onFoundNearest, error);
    }
  };

  useEffect(() => {
    if (pointTwo && pointOne) {
      axios
        .post(
          "http://localhost:4567/croute",
          {
            node1Id: pointOne.id,
            node2Id: pointTwo.id,
          },
          {
            headers: {
              "Content-Type": "application/json",
              "Access-Control-Allow-Origin": "*",
            },
          }
        )
        .then((res) => {
          setActiveRoute([]);
          const routeWays = res.data.route;
          routeWays.forEach((routeWay) => {
            setActiveRoute((actRoute) => {
              return actRoute.concat({
                start: { latitude: routeWay[0], longitude: routeWay[1] },
                end: { latitude: routeWay[2], longitude: routeWay[3] },
              });
            });
          });
        })
        .catch((err) => {
          setError("Error: Could not retrieve route from server")
        });
    }
  }, [pointTwo, pointOne]);

  useEffect(() => {
    document.addEventListener("dblclick", onDoubleClick);
    return () => {
      document.removeEventListener("dblclick", onDoubleClick);
    };
  });

  useEffect(() => {
    setActiveWays([]);
    const getWays = () => {
      const tiles = getActiveTiles();
      tiles.forEach((tile) => {
        const tileId = JSON.stringify(tile);
        const tileWays = JSON.parse(localStorage.getItem(tileId));
        if (tileWays) {
          setActiveWays((actWays) => {
            return actWays.concat(tileWays);
          });
        } else {
          requestWays(tile);
        }
      });
    };
    getWays();
  }, [corners]);

  //DRAW CANVAS
  useEffect(() => {
    const canvas = canvasRef.current;

    canvas.onmousewheel = (e) => {
      onZoom(e.wheelDelta);
    };
    const context = canvas.getContext("2d");
    draw(context);

    context.strokeStyle = "#FFFFFF";

    context.beginPath();
    context.lineWidth = 3;
    activeWays.map((way) => {
      const wayObj = {
        start: { latitude: way[0], longitude: way[1] },
        end: { latitude: way[2], longitude: way[3] },
      };
      drawWay(context, wayObj);
    });
    context.stroke();

    context.strokeStyle = "#FF0000";
    context.lineWidth = 5;

    if (pointOne) {
      const n1 = getPointFromCoords(pointOne.coords);
      context.beginPath();
      context.strokeStyle = "blue";
      context.arc(n1.x, n1.y, 10, 0, Math.PI * 2, true);
      context.stroke();
    }

    if (pointTwo) {
      const n2 = getPointFromCoords(pointTwo.coords);
      context.beginPath();
      context.strokeStyle = "red";
      context.arc(n2.x, n2.y, 10, 0, Math.PI * 2, true);
      context.stroke();
    }

    if (activeRoute.length != 0) {
      //DRAW ROUTE
      context.beginPath();
      context.strokeStyle = "black";
      activeRoute.forEach((way) => {
        drawWay(context, way);
      });
      context.stroke();
    }
  }, [activeWays, pointOne, pointTwo, activeRoute]);

  return (
    <>
      <Route
        title="Maps!"
        pointOne={pointOne}
        pointTwo={pointTwo}
        setPointOne={setPointOne}
        setPointTwo={setPointTwo}
        setActiveRoute={setActiveRoute}
        setError={setError}
      />
      <canvas ref={canvasRef} style={{ zIndex: 20 }} />
    </>
  );
};

export default Map;
